package com.gdpr.unir.exception.impl;

import com.gdpr.unir.exception.AbstractServiceException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationException extends AbstractServiceException {
    private static final String CODE = "001-0500";
    private static final String ERROR_MESSAGE_CODE = "global.error.validation-exception-message";

    @Getter
    private List<ValidationError> validationErrors;

    private ValidationException(final String message,
                                final Throwable cause,
                                final Map<String, Object> additionalInformation,
                                final List<ValidationError> validationErrors) {
        super(message, cause, CODE, additionalInformation, ERROR_MESSAGE_CODE, null);
        this.validationErrors = validationErrors;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String message;
        private List<ValidationError> validationErrors;
        private Map<String, Object> additionalInformation;

        private Builder() {
        }

        public ValidationException.Builder message(final String message) {
            this.message = message;
            return this;
        }

        public ValidationException.Builder addAdditionalInformation(final String key, final Object value) {
            if (additionalInformation == null) {
                additionalInformation = new HashMap<>();
            }

            additionalInformation.put(key, value);

            return this;
        }

        public ValidationException.Builder addValidationErrors(final List<ValidationError> validationErrors) {
            if (this.validationErrors == null) {
                this.validationErrors = new ArrayList<>();
            }

            this.validationErrors.addAll(validationErrors);
            return this;
        }

        public ValidationException.Builder addValidationError(final String path, final String errorMessageCode, final Object... args) {
            if (validationErrors == null) {
                validationErrors = new ArrayList<>();
            }

            final ValidationError validationError = new ValidationError(path, errorMessageCode, args);
            validationErrors.add(validationError);
            return this;
        }

        public ValidationException build() {
            return new ValidationException(message, null, additionalInformation, validationErrors);
        }
    }
}
