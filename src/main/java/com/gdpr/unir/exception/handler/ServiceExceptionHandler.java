package com.gdpr.unir.exception.handler;

import com.gdpr.unir.exception.AbstractServiceException;
import com.gdpr.unir.exception.ErrorMessage;
import com.gdpr.unir.exception.impl.ConfigurationException;
import com.gdpr.unir.exception.impl.IllegalStateException;
import com.gdpr.unir.exception.impl.InternalInconsistencyException;
import com.gdpr.unir.exception.impl.ResourceNotFoundException;
import com.gdpr.unir.exception.impl.ValidationError;
import com.gdpr.unir.exception.impl.ValidationException;
import com.gdpr.unir.exception.resource.ErrorItemResource;
import com.gdpr.unir.exception.resource.ErrorMessageResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.context.i18n.LocaleContextHolder.getLocale;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@ControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String RUNTIME_EXCEPTION_CODE = "001-1000";
    private static final String RUNTIME_EXCEPTION_MESSAGE_CODE = "global.error.unknown";

    private static final String HTTP_MESSAGE_NOT_READABLE_EXCEPTION_CODE = "001-1100";
    private static final String HTTP_MESSAGE_NOT_READABLE_EXCEPTION_MESSAGE_CODE = "global.error.invalid-format";

    private final MessageSource messageSource;

    public ServiceExceptionHandler(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException exception,
                                                               final HttpHeaders headers,
                                                               final HttpStatus status,
                                                               final WebRequest request) {
        log.debug("MethodArgumentNotValidException has been thrown " + exception, exception);

        final List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        final List<ValidationError> validationErrors = fieldErrors.stream()
                .map(fieldError -> new ValidationError(convertFromCamelCaseToSnakeCase(fieldError.getField()), fieldError.getDefaultMessage()))
                .collect(toList());

        final ValidationException validationException = ValidationException.builder()
                .addValidationErrors(validationErrors)
                .build();
        final ErrorMessageResource errorMessageResource = getErrorMessageResourceWithItems(validationException);

        return ResponseEntity
                .status(UNPROCESSABLE_ENTITY)
                .body(errorMessageResource);
    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException exception,
                                                               final HttpHeaders headers,
                                                               final HttpStatus status,
                                                               final WebRequest request) {
        log.warn("HttpMessageNotReadableException has been thrown.", exception);

        final ErrorMessageResource errorMessageResource = getErrorMessageResource(HTTP_MESSAGE_NOT_READABLE_EXCEPTION_CODE,
                HTTP_MESSAGE_NOT_READABLE_EXCEPTION_MESSAGE_CODE, null);
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(errorMessageResource);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ErrorMessageResource> handleRuntimeException(final RuntimeException exception) {
        log.error("Unhandled RuntimeException has been thrown. " + exception, exception);

        final ErrorMessageResource errorMessageResource = getErrorMessageResource(RUNTIME_EXCEPTION_CODE,
                RUNTIME_EXCEPTION_MESSAGE_CODE, null);
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(errorMessageResource);
    }

    @ExceptionHandler({InternalInconsistencyException.class, ConfigurationException.class})
    public ResponseEntity<ErrorMessageResource> handleInternalServerError(final AbstractServiceException exception) {
        log.error("Fatal exception has been thrown. " + exception, exception);

        final ErrorMessageResource errorMessageResource = getErrorMessageResource(exception);
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(errorMessageResource);
    }

    @ExceptionHandler({IllegalStateException.class})
    public ResponseEntity<ErrorMessageResource> handleIllegalStateExceptions(final IllegalStateException exception) {
        log.debug("IllegalState exception has been thrown.", exception);

        final ErrorMessageResource errorMessageResource = getErrorMessageResource(exception);
        return ResponseEntity
                .status(CONFLICT)
                .body(errorMessageResource);
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<ErrorMessageResource> handleValidationExceptions(final ValidationException exception) {
        log.debug("Validation exception has been thrown. " + exception, exception);

        final ErrorMessageResource errorMessageResource = getErrorMessageResourceWithItems(exception);
        return ResponseEntity
                .status(UNPROCESSABLE_ENTITY)
                .body(errorMessageResource);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<ErrorMessageResource> handleNotFoundException(AbstractServiceException exception) {
        log.debug("Resource not found exception has been thrown. " + exception, exception);

        final ErrorMessageResource errorMessageResource = getErrorMessageResource(exception);

        return ResponseEntity
                .status(NOT_FOUND)
                .body(errorMessageResource);
    }

    private ErrorMessageResource getErrorMessageResource(final AbstractServiceException abstractServiceException) {
        final ErrorMessage errorMessage = abstractServiceException.getErrorMessage();
        return getErrorMessageResource(abstractServiceException.getCode(), errorMessage.getCode(), errorMessage.getArgs());
    }

    private ErrorMessageResource getErrorMessageResource(final String code, final String messageCode, final Object[] args) {
        final String message = messageSource.getMessage(messageCode, args, messageCode, getLocale());

        return ErrorMessageResource.builder()
                .code(code)
                .message(message)
                .build();
    }

    private ErrorMessageResource getErrorMessageResourceWithItems(final ValidationException validationException) {
        final ErrorMessageResource errorMessageResource = getErrorMessageResource(validationException);
        final List<ValidationError> validationErrors = validationException.getValidationErrors();

        if (validationErrors == null || validationErrors.isEmpty()) {
            return errorMessageResource;
        }

        errorMessageResource.setErrors(validationErrors.stream()
                .map(this::getErrorItemResource)
                .collect(toList()));

        return errorMessageResource;
    }

    private ErrorItemResource getErrorItemResource(final ValidationError validationError) {
        final ErrorMessage errorMessage = validationError.getErrorMessage();
        final String messageCode = errorMessage.getCode();
        final String validationMessage = messageSource.getMessage(messageCode, errorMessage.getArgs(), messageCode, getLocale());

        return new ErrorItemResource(validationError.getPath(), validationMessage);
    }

    private String convertFromCamelCaseToSnakeCase(final String text) {
        return text.replaceAll("(.)(\\p{Upper})", "$1_$2").toLowerCase();
    }
}
