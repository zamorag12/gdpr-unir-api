package com.gdpr.unir.exception.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@Setter
@Builder
@JsonInclude(NON_NULL)
public class ErrorMessageResource {
    private String code;
    private String message;
    private List<ErrorItemResource> errors;
}
