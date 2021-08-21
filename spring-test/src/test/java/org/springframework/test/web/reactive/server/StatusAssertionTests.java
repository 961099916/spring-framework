/*
 * Copyright 2002-2021 the original author or authors.
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

package org.springframework.test.web.reactive.server;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.Mockito.mock;

import java.net.URI;
import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.client.reactive.MockClientHttpRequest;
import org.springframework.mock.http.client.reactive.MockClientHttpResponse;

import reactor.core.publisher.MonoProcessor;

/**
 * Unit tests for {@link StatusAssertions}.
 *
 * @author Rossen Stoyanchev
 */
public class StatusAssertionTests {

    @Test
    public void isEqualTo() {
        StatusAssertions assertions = statusAssertions(HttpStatus.CONFLICT);

        // Success
        assertions.isEqualTo(HttpStatus.CONFLICT);
        assertions.isEqualTo(409);

        // Wrong status
        assertThatExceptionOfType(AssertionError.class)
            .isThrownBy(() -> assertions.isEqualTo(HttpStatus.REQUEST_TIMEOUT));

        // Wrong status value
        assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertions.isEqualTo(408));
    }

    @Test // gh-23630
    public void isEqualToWithCustomStatus() {
        statusAssertions(600).isEqualTo(600);
    }

    @Test
    public void reasonEquals() {
        StatusAssertions assertions = statusAssertions(HttpStatus.CONFLICT);

        // Success
        assertions.reasonEquals("Conflict");

        // Wrong reason
        assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertions.reasonEquals("Request Timeout"));
    }

    @Test
    public void statusSeries1xx() {
        StatusAssertions assertions = statusAssertions(HttpStatus.CONTINUE);

        // Success
        assertions.is1xxInformational();

        // Wrong series
        assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertions.is2xxSuccessful());
    }

    @Test
    public void statusSeries2xx() {
        StatusAssertions assertions = statusAssertions(HttpStatus.OK);

        // Success
        assertions.is2xxSuccessful();

        // Wrong series
        assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertions.is5xxServerError());
    }

    @Test
    public void statusSeries3xx() {
        StatusAssertions assertions = statusAssertions(HttpStatus.PERMANENT_REDIRECT);

        // Success
        assertions.is3xxRedirection();

        // Wrong series
        assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertions.is2xxSuccessful());
    }

    @Test
    public void statusSeries4xx() {
        StatusAssertions assertions = statusAssertions(HttpStatus.BAD_REQUEST);

        // Success
        assertions.is4xxClientError();

        // Wrong series
        assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertions.is2xxSuccessful());
    }

    @Test
    public void statusSeries5xx() {
        StatusAssertions assertions = statusAssertions(HttpStatus.INTERNAL_SERVER_ERROR);

        // Success
        assertions.is5xxServerError();

        // Wrong series
        assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertions.is2xxSuccessful());
    }

    @Test
    public void matchesStatusValue() {
        StatusAssertions assertions = statusAssertions(HttpStatus.CONFLICT);

        // Success
        assertions.value(equalTo(409));
        assertions.value(greaterThan(400));

        // Wrong status
        assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertions.value(equalTo(200)));
    }

    @Test // gh-26658
    public void matchesCustomStatusValue() {
        statusAssertions(600).value(equalTo(600));
    }

    private StatusAssertions statusAssertions(HttpStatus status) {
        return statusAssertions(status.value());
    }

    private StatusAssertions statusAssertions(int status) {
        MockClientHttpRequest request = new MockClientHttpRequest(HttpMethod.GET, URI.create("/"));
        MockClientHttpResponse response = new MockClientHttpResponse(status);

        MonoProcessor<byte[]> emptyContent = MonoProcessor.create();
        emptyContent.onComplete();

        ExchangeResult result = new ExchangeResult(request, response, emptyContent, emptyContent, Duration.ZERO, null);
        return new StatusAssertions(result, mock(WebTestClient.ResponseSpec.class));
    }

}
