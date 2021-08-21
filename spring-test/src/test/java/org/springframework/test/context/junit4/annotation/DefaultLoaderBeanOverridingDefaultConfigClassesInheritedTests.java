/*
 * Copyright 2002-2019 the original author or authors.
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

package org.springframework.test.context.junit4.annotation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.testfixture.beans.Employee;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.DelegatingSmartContextLoader;

/**
 * Integration tests that verify support for configuration classes in the Spring TestContext Framework in conjunction
 * with the {@link DelegatingSmartContextLoader}.
 *
 * @author Sam Brannen
 * @since 3.1
 */
@ContextConfiguration
public class DefaultLoaderBeanOverridingDefaultConfigClassesInheritedTests
    extends DefaultLoaderDefaultConfigClassesBaseTests {

    @Test
    @Override
    public void verifyEmployeeSetFromBaseContextConfig() {
        assertThat(this.employee).as("The employee should have been autowired.").isNotNull();
        assertThat(this.employee.getName()).as("The employee bean should have been overridden.").isEqualTo("Yoda");
    }

    @Configuration
    static class Config {

        @Bean
        public Employee employee() {
            Employee employee = new Employee();
            employee.setName("Yoda");
            employee.setAge(900);
            employee.setCompany("The Force");
            return employee;
        }
    }

}
