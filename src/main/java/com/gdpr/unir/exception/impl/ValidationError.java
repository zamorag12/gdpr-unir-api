package com.gdpr.unir.exception.impl;

import com.gdpr.unir.exception.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ValidationError {
    private String path;
    private ErrorMessage errorMessage;

    public ValidationError(String path, String errorMessageCode, Object... args) {
        this.path = path;
        this.errorMessage = new ErrorMessage(errorMessageCode, args);
    }
}