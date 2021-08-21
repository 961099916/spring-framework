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

package org.springframework.web.reactive.result.condition;

import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.NotAcceptableStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

/**
 * Supports media type expressions as described in: {@link RequestMapping#consumes()} and
 * {@link RequestMapping#produces()}.
 *
 * @author Rossen Stoyanchev
 * @since 5.0
 */
abstract class AbstractMediaTypeExpression implements Comparable<AbstractMediaTypeExpression>, MediaTypeExpression {

    private final MediaType mediaType;

    private final boolean isNegated;

    AbstractMediaTypeExpression(String expression) {
        if (expression.startsWith("!")) {
            this.isNegated = true;
            expression = expression.substring(1);
        } else {
            this.isNegated = false;
        }
        this.mediaType = MediaType.parseMediaType(expression);
    }

    AbstractMediaTypeExpression(MediaType mediaType, boolean negated) {
        this.mediaType = mediaType;
        this.isNegated = negated;
    }

    @Override
    public MediaType getMediaType() {
        return this.mediaType;
    }

    @Override
    public boolean isNegated() {
        return this.isNegated;
    }

    public final boolean match(ServerWebExchange exchange) {
        try {
            boolean match = matchMediaType(exchange);
            return (!this.isNegated == match);
        } catch (NotAcceptableStatusException | UnsupportedMediaTypeStatusException ex) {
            return false;
        }
    }

    protected abstract boolean matchMediaType(ServerWebExchange exchange)
        throws NotAcceptableStatusException, UnsupportedMediaTypeStatusException;

    @Override
    public int compareTo(AbstractMediaTypeExpression other) {
        return MediaType.SPECIFICITY_COMPARATOR.compare(this.getMediaType(), other.getMediaType());
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        AbstractMediaTypeExpression otherExpr = (AbstractMediaTypeExpression)other;
        return (this.mediaType.equals(otherExpr.mediaType) && this.isNegated == otherExpr.isNegated);
    }

    @Override
    public int hashCode() {
        return this.mediaType.hashCode();
    }

    @Override
    public String toString() {
        if (this.isNegated) {
            return '!' + this.mediaType.toString();
        }
        return this.mediaType.toString();
    }

}
