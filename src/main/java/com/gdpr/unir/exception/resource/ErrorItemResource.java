package com.gdpr.unir.exception.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorItemResource {
    private String path;
    private String message;
}
