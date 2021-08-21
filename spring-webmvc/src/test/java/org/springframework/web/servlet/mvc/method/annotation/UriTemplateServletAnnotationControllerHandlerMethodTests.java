/*
 * Copyright 2002-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.springframework.web.servlet.mvc.method.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.AbstractView;
import org.springframework.web.testfixture.servlet.MockHttpServletRequest;
import org.springframework.web.testfixture.servlet.MockHttpServletResponse;

/**
 * @author Rossen Stoyanchev
 * @since 3.1
 */
public class UriTemplateServletAnnotationControllerHandlerMethodTests extends AbstractServletHandlerMethodTests {

    @Test
    public void simple() throws Exception {
        initServletWithControllers(SimpleUriTemplateController.class);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/42");
        MockHttpServletResponse response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getContentAsString()).isEqualTo("test-42-7");
    }

    @Test // gh-25864
    public void literalMappingWithPathParams() throws Exception {
        initServletWithControllers(MultipleUriTemplateController.class);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/data");
        MockHttpServletResponse response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).isEqualTo("test");

        request = new MockHttpServletRequest("GET", "/data;foo=bar");
        response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getStatus()).isEqualTo(404);

        request = new MockHttpServletRequest("GET", "/data;jsessionid=123");
        response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).isEqualTo("test");
    }

    @Test
    public void multiple() throws Exception {
        initServletWithControllers(MultipleUriTemplateController.class);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/hotels/42;q=24/bookings/21-other;q=12");
        MockHttpServletResponse response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).isEqualTo("test-42-q24-21-other-q12");
    }

    @Test
    public void pathVarsInModel() throws Exception {
        final Map<String, Object> pathVars = new HashMap<>();
        pathVars.put("hotel", "42");
        pathVars.put("booking", 21);
        pathVars.put("other", "other");

        WebApplicationContext wac = initServlet(new ApplicationContextInitializer<GenericWebApplicationContext>() {
            @Override
            public void initialize(GenericWebApplicationContext context) {
                RootBeanDefinition beanDef = new RootBeanDefinition(ModelValidatingViewResolver.class);
                beanDef.getConstructorArgumentValues().addGenericArgumentValue(pathVars);
                context.registerBeanDefinition("viewResolver", beanDef);
            }
        }, ViewRenderingController.class);

        MockHttpServletRequest request =
            new MockHttpServletRequest("GET", "/hotels/42;q=1,2/bookings/21-other;q=3;r=R");
        getServlet().service(request, new MockHttpServletResponse());

        ModelValidatingViewResolver resolver = wac.getBean(ModelValidatingViewResolver.class);
        assertThat(resolver.validatedAttrCount).isEqualTo(3);
    }

    @Test
    public void binding() throws Exception {
        initServletWithControllers(BindingUriTemplateController.class);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/hotels/42/dates/2008-11-18");
        MockHttpServletResponse response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getStatus()).isEqualTo(200);

        request = new MockHttpServletRequest("GET", "/hotels/42/dates/2008-foo-bar");
        response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getStatus()).isEqualTo(400);

        initServletWithControllers(NonBindingUriTemplateController.class);
        request = new MockHttpServletRequest("GET", "/hotels/42/dates/2008-foo-bar");
        response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getStatus()).isEqualTo(500);
    }

    @Test
    public void ambiguous() throws Exception {
        initServletWithControllers(AmbiguousUriTemplateController.class);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/hotels/new");
        MockHttpServletResponse response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getContentAsString()).isEqualTo("specific");
    }

    @Test
    public void relative() throws Exception {
        initServletWithControllers(RelativePathUriTemplateController.class);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/hotels/42/bookings/21");
        MockHttpServletResponse response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getContentAsString()).isEqualTo("test-42-21");

        request = new MockHttpServletRequest("GET", "/hotels/42/bookings/21.html");
        response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getContentAsString()).isEqualTo("test-42-21");
    }

    @Test
    public void extension() throws Exception {
        initServletWithControllers(SimpleUriTemplateController.class);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/42;jsessionid=c0o7fszeb1;q=24.xml");
        MockHttpServletResponse response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getContentAsString()).isEqualTo("test-42-24");

    }

    @Test
    public void typeConversionError() throws Exception {
        initServletWithControllers(SimpleUriTemplateController.class);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/foo.xml");
        MockHttpServletResponse response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getStatus()).as("Invalid response status code")
            .isEqualTo(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void explicitSubPath() throws Exception {
        initServletWithControllers(ExplicitSubPathController.class);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/hotels/42");
        MockHttpServletResponse response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getContentAsString()).isEqualTo("test-42");
    }

    @Test
    public void implicitSubPath() throws Exception {
        initServletWithControllers(ImplicitSubPathController.class);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/hotels/42");
        MockHttpServletResponse response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getContentAsString()).isEqualTo("test-42");
    }

    @Test
    public void crud() throws Exception {
        initServletWithControllers(CrudController.class);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/hotels");
        MockHttpServletResponse response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getContentAsString()).isEqualTo("list");

        request = new MockHttpServletRequest("GET", "/hotels/");
        response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getContentAsString()).isEqualTo("list");

        request = new MockHttpServletRequest("POST", "/hotels");
        response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getContentAsString()).isEqualTo("create");

        request = new MockHttpServletRequest("GET", "/hotels/42");
        response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getContentAsString()).isEqualTo("show-42");

        request = new MockHttpServletRequest("GET", "/hotels/42/");
        response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getContentAsString()).isEqualTo("show-42");

        request = new MockHttpServletRequest("PUT", "/hotels/42");
        response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getContentAsString()).isEqualTo("createOrUpdate-42");

        request = new MockHttpServletRequest("DELETE", "/hotels/42");
        response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getContentAsString()).isEqualTo("remove-42");
    }

    @Test
    public void methodNotSupported() throws Exception {
        initServletWithControllers(MethodNotAllowedController.class);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/hotels/1");
        MockHttpServletResponse response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getStatus()).isEqualTo(200);

        request = new MockHttpServletRequest("POST", "/hotels/1");
        response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getStatus()).isEqualTo(405);

        request = new MockHttpServletRequest("GET", "/hotels");
        response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getStatus()).isEqualTo(200);

        request = new MockHttpServletRequest("POST", "/hotels");
        response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getStatus()).isEqualTo(405);
    }

    @Test
    public void multiPaths() throws Exception {
        initServletWithControllers(MultiPathController.class);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/category/page/5");
        MockHttpServletResponse response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getContentAsString()).isEqualTo("handle4-page-5");

        request = new MockHttpServletRequest("GET", "/category/page/5.html");
        response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getContentAsString()).isEqualTo("handle4-page-5");
    }

    @Test
    public void customRegex() throws Exception {
        initServletWithControllers(CustomRegexController.class);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/42;q=1;q=2");
        MockHttpServletResponse response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentAsString()).isEqualTo("test-42-;q=1;q=2-[1, 2]");
    }

    /*
     * See SPR-6640
     */
    @Test
    public void menuTree() throws Exception {
        initServletWithControllers(MenuTreeController.class);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/book/menu/type/M5");
        MockHttpServletResponse response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getContentAsString()).isEqualTo("M5");
    }

    /*
     * See SPR-6876
     */
    @Test
    public void variableNames() throws Exception {
        initServletWithControllers(VariableNamesController.class);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test/foo");
        MockHttpServletResponse response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getContentAsString()).isEqualTo("foo-foo");

        request = new MockHttpServletRequest("DELETE", "/test/bar");
        response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getContentAsString()).isEqualTo("bar-bar");
    }

    /*
     * See SPR-8543
     */
    @Test
    public void variableNamesWithUrlExtension() throws Exception {
        initServletWithControllers(VariableNamesController.class);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test/foo.json");
        MockHttpServletResponse response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getContentAsString()).isEqualTo("foo-foo");
    }

    /*
     * See SPR-6978
     */
    @Test
    public void doIt() throws Exception {
        initServletWithControllers(Spr6978Controller.class);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/foo/100");
        MockHttpServletResponse response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getContentAsString()).isEqualTo("loadEntity:foo:100");

        request = new MockHttpServletRequest("POST", "/foo/100");
        response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getContentAsString()).isEqualTo("publish:foo:100");

        request = new MockHttpServletRequest("GET", "/module/100");
        response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getContentAsString()).isEqualTo("loadModule:100");

        request = new MockHttpServletRequest("POST", "/module/100");
        response = new MockHttpServletResponse();
        getServlet().service(request, response);
        assertThat(response.getContentAsString()).isEqualTo("publish:module:100");

    }

    /*
     * Controllers
     */

    @Controller
    public static class SimpleUriTemplateController {

        @RequestMapping("/{root}")
        public void handle(@PathVariable("root") int root, @MatrixVariable(required = false, defaultValue = "7") int q,
            Writer writer) throws IOException {

            assertThat(root).as("Invalid path variable value").isEqualTo(42);
            writer.write("test-" + root + "-" + q);
        }

    }

    @Controller
    public static class MultipleUriTemplateController {

        @RequestMapping("/hotels/{hotel}/bookings/{booking}-{other}")
        public void handle(@PathVariable("hotel") String hotel, @PathVariable int booking, @PathVariable String other,
            @MatrixVariable(name = "q", pathVar = "hotel") int qHotel,
            @MatrixVariable(name = "q", pathVar = "other") int qOther, Writer writer) throws IOException {
            assertThat(hotel).as("Invalid path variable value").isEqualTo("42");
            assertThat(booking).as("Invalid path variable value").isEqualTo(21);
            writer.write("test-" + hotel + "-q" + qHotel + "-" + booking + "-" + other + "-q" + qOther);
        }

        @RequestMapping("/data")
        void handleWithLiteralMapping(Writer writer) throws IOException {
            writer.write("test");
        }
    }

    @Controller
    public static class ViewRenderingController {

        @RequestMapping("/hotels/{hotel}/bookings/{booking}-{other}")
        public void handle(@PathVariable("hotel") String hotel, @PathVariable int booking, @PathVariable String other,
            @MatrixVariable MultiValueMap<String, String> params) {

            assertThat(hotel).as("Invalid path variable value").isEqualTo("42");
            assertThat(booking).as("Invalid path variable value").isEqualTo(21);
            assertThat(params.get("q")).isEqualTo(Arrays.asList("1", "2", "3"));
            assertThat(params.getFirst("r")).isEqualTo("R");
        }

    }

    @Controller
    public static class BindingUriTemplateController {

        @InitBinder
        public void initBinder(WebDataBinder binder, @PathVariable("hotel") String hotel) {
            assertThat(hotel).as("Invalid path variable value").isEqualTo("42");
            binder.initBeanPropertyAccess();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
        }

        @RequestMapping("/hotels/{hotel}/dates/{date}")
        public void handle(@PathVariable("hotel") String hotel, @PathVariable Date date, Writer writer)
            throws IOException {
            assertThat(hotel).as("Invalid path variable value").isEqualTo("42");
            assertThat(date).as("Invalid path variable value").isEqualTo(new GregorianCalendar(2008, 10, 18).getTime());
            writer.write("test-" + hotel);
        }
    }

    @Controller
    public static class NonBindingUriTemplateController {

        @RequestMapping("/hotels/{hotel}/dates/{date}")
        public void handle(@PathVariable("hotel") String hotel, @PathVariable Date date, Writer writer)
            throws IOException {}

    }

    @Controller
    @RequestMapping("/hotels/{hotel}")
    public static class RelativePathUriTemplateController {

        @RequestMapping("bookings/{booking}")
        public void handle(@PathVariable("hotel") String hotel, @PathVariable int booking, Writer writer)
            throws IOException {
            assertThat(hotel).as("Invalid path variable value").isEqualTo("42");
            assertThat(booking).as("Invalid path variable value").isEqualTo(21);
            writer.write("test-" + hotel + "-" + booking);
        }

    }

    @Controller
    @RequestMapping("/hotels")
    public static class AmbiguousUriTemplateController {

        @RequestMapping("/{hotel}")
        public void handleVars(@PathVariable("hotel") String hotel, Writer writer) throws IOException {
            assertThat(hotel).as("Invalid path variable value").isEqualTo("42");
            writer.write("variables");
        }

        @RequestMapping("/new")
        public void handleSpecific(Writer writer) throws IOException {
            writer.write("specific");
        }

        @RequestMapping("/*")
        public void handleWildCard(Writer writer) throws IOException {
            writer.write("wildcard");
        }

    }

    @Controller
    @RequestMapping("/hotels/*")
    public static class ExplicitSubPathController {

        @RequestMapping("{hotel}")
        public void handleHotel(@PathVariable String hotel, Writer writer) throws IOException {
            writer.write("test-" + hotel);
        }

    }

    @Controller
    @RequestMapping("hotels")
    public static class ImplicitSubPathController {

        @RequestMapping("{hotel}")
        public void handleHotel(@PathVariable String hotel, Writer writer) throws IOException {
            writer.write("test-" + hotel);
        }
    }

    @Controller
    public static class CustomRegexController {

        @RequestMapping("/{root:\\d+}{params}")
        public void handle(@PathVariable("root") int root, @PathVariable("params") String paramString,
            @MatrixVariable List<Integer> q, Writer writer) throws IOException {

            assertThat(root).as("Invalid path variable value").isEqualTo(42);
            writer.write("test-" + root + "-" + paramString + "-" + q);
        }
    }

    @Controller
    public static class DoubleController {

        @RequestMapping("/lat/{latitude}/long/{longitude}")
        public void testLatLong(@PathVariable Double latitude, @PathVariable Double longitude, Writer writer)
            throws IOException {
            writer.write("latitude-" + latitude + "-longitude-" + longitude);
        }
    }

    @Controller
    @RequestMapping("hotels")
    public static class CrudController {

        @RequestMapping(method = RequestMethod.GET)
        public void list(Writer writer) throws IOException {
            writer.write("list");
        }

        @RequestMapping(method = RequestMethod.POST)
        public void create(Writer writer) throws IOException {
            writer.write("create");
        }

        @RequestMapping(value = "/{hotel}", method = RequestMethod.GET)
        public void show(@PathVariable String hotel, Writer writer) throws IOException {
            writer.write("show-" + hotel);
        }

        @RequestMapping(value = "{hotel}", method = RequestMethod.PUT)
        public void createOrUpdate(@PathVariable String hotel, Writer writer) throws IOException {
            writer.write("createOrUpdate-" + hotel);
        }

        @RequestMapping(value = "{hotel}", method = RequestMethod.DELETE)
        public void remove(@PathVariable String hotel, Writer writer) throws IOException {
            writer.write("remove-" + hotel);
        }

    }

    @Controller
    @RequestMapping("/hotels")
    public static class MethodNotAllowedController {

        @RequestMapping(method = RequestMethod.GET)
        public void list(Writer writer) {}

        @RequestMapping(method = RequestMethod.GET, value = "{hotelId}")
        public void show(@PathVariable long hotelId, Writer writer) {}

        @RequestMapping(method = RequestMethod.PUT, value = "{hotelId}")
        public void createOrUpdate(@PathVariable long hotelId, Writer writer) {}

        @RequestMapping(method = RequestMethod.DELETE, value = "/{hotelId}")
        public void remove(@PathVariable long hotelId, Writer writer) {}
    }

    @Controller
    @RequestMapping("/category")
    public static class MultiPathController {

        @RequestMapping(value = {"/{category}/page/{page}", "/*/{category}/page/{page}"})
        public void category(@PathVariable String category, @PathVariable int page, Writer writer) throws IOException {
            writer.write("handle1-");
            writer.write("category-" + category);
            writer.write("page-" + page);
        }

        @RequestMapping(value = {"/{category}", "/*/{category}"})
        public void category(@PathVariable String category, Writer writer) throws IOException {
            writer.write("handle2-");
            writer.write("category-" + category);
        }

        @RequestMapping(value = {""})
        public void category(Writer writer) throws IOException {
            writer.write("handle3");
        }

        @RequestMapping(value = {"/page/{page}"})
        public void category(@PathVariable int page, Writer writer) throws IOException {
            writer.write("handle4-");
            writer.write("page-" + page);
        }

    }

    @Controller
    @RequestMapping("/*/menu/") // was /*/menu/**
    public static class MenuTreeController {

        @RequestMapping("type/{var}")
        public void getFirstLevelFunctionNodes(@PathVariable("var") String var, Writer writer) throws IOException {
            writer.write(var);
        }
    }

    @Controller
    @RequestMapping("/test")
    public static class VariableNamesController {

        @RequestMapping(value = "/{foo}", method = RequestMethod.GET)
        public void foo(@PathVariable String foo, Writer writer) throws IOException {
            writer.write("foo-" + foo);
        }

        @RequestMapping(value = "/{bar}", method = RequestMethod.DELETE)
        public void bar(@PathVariable String bar, Writer writer) throws IOException {
            writer.write("bar-" + bar);
        }
    }

    @Controller
    public static class Spr6978Controller {

        @RequestMapping(value = "/{type}/{id}", method = RequestMethod.GET)
        public void loadEntity(@PathVariable final String type, @PathVariable final long id, Writer writer)
            throws IOException {
            writer.write("loadEntity:" + type + ":" + id);
        }

        @RequestMapping(value = "/module/{id}", method = RequestMethod.GET)
        public void loadModule(@PathVariable final long id, Writer writer) throws IOException {
            writer.write("loadModule:" + id);
        }

        @RequestMapping(value = "/{type}/{id}", method = RequestMethod.POST)
        public void publish(@PathVariable final String type, @PathVariable final long id, Writer writer)
            throws IOException {
            writer.write("publish:" + type + ":" + id);
        }
    }

    public static class ModelValidatingViewResolver implements ViewResolver {

        private final Map<String, Object> attrsToValidate;

        int validatedAttrCount;

        public ModelValidatingViewResolver(Map<String, Object> attrsToValidate) {
            this.attrsToValidate = attrsToValidate;
        }

        @Override
        public View resolveViewName(final String viewName, Locale locale) throws Exception {
            return new AbstractView() {
                @Override
                public String getContentType() {
                    return null;
                }

                @Override
                protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
                    HttpServletResponse response) throws Exception {
                    for (String key : attrsToValidate.keySet()) {
                        assertThat(model.containsKey(key)).as("Model should contain attribute named " + key).isTrue();
                        assertThat(model.get(key)).isEqualTo(attrsToValidate.get(key));
                        validatedAttrCount++;
                    }
                }
            };
        }
    }

    // @Disabled("ControllerClassNameHandlerMapping")
    // public void controllerClassName() throws Exception {

    // @Disabled("useDefaultSuffixPattern property not supported")
    // public void doubles() throws Exception {

    // @Disabled("useDefaultSuffixPattern property not supported")
    // public void noDefaultSuffixPattern() throws Exception {

}
