package com.gdpr.unir.exception.handler;

import com.gdpr.unir.exception.impl.ValidationException;
import com.gdpr.unir.exception.resource.ErrorItemResource;
import com.gdpr.unir.exception.resource.ErrorMessageResource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.context.i18n.LocaleContextHolder.getLocale;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RunWith(MockitoJUnitRunner.class)
public class ServiceExceptionHandlerTest {

    private static final String ERROR_MESSAGE_CODE = "fake.code";
    private static final String ERROR_MESSAGE = "fake.message";

    private static final String HTTP_MESSAGE_NOT_READABLE_EXCEPTION_CODE = "001-1200";
    private static final String HTTP_MESSAGE_NOT_READABLE_EXCEPTION_MESSAGE_CODE = "global.error.invalid-format";

    private static final String VALIDATION_EXCEPTION_CODE = "001-0500";
    private final static String VALIDATION_EXCEPTION_MESSAGE_CODE = "global.error.validation-exception-message";

    private ServiceExceptionHandler subject;

    @Mock
    private MessageSource messageSource;

    @Before
    public void setUp() {
        subject = new ServiceExceptionHandler(messageSource);
    }

    @Test
    public void handleHttpMessageNotReadableException_HttpMessageNotReadableException_BadRequestResponse() {
        final HttpMessageNotReadableException httpMessageNotReadableException = new HttpMessageNotReadableException(ERROR_MESSAGE);

        when(messageSource.getMessage(HTTP_MESSAGE_NOT_READABLE_EXCEPTION_MESSAGE_CODE, null, HTTP_MESSAGE_NOT_READABLE_EXCEPTION_MESSAGE_CODE, getLocale())).thenReturn(ERROR_MESSAGE);

        final ResponseEntity<Object> response = subject.handleHttpMessageNotReadable(httpMessageNotReadableException, null, null, null);

        verify(messageSource).getMessage(HTTP_MESSAGE_NOT_READABLE_EXCEPTION_MESSAGE_CODE, null, HTTP_MESSAGE_NOT_READABLE_EXCEPTION_MESSAGE_CODE, getLocale());

        final Object responseBody = response.getBody();

        assertThat(responseBody, instanceOf(ErrorMessageResource.class));

        final ErrorMessageResource errorMessageResource = (ErrorMessageResource) responseBody;

        assertThat(response.getStatusCode(), is(BAD_REQUEST));
        assertThat(errorMessageResource.getMessage(), is(ERROR_MESSAGE));
    }

    @Test
    public void handleMethodArgumentNotValidException_WithFieldErrors_UnProcessableEntityWithErrorResponse() throws NoSuchMethodException {
        final String validationExceptionMessage = randomAlphabetic(10);
        final String errorPath = "testPath";

        when(messageSource.getMessage(VALIDATION_EXCEPTION_MESSAGE_CODE, null, VALIDATION_EXCEPTION_MESSAGE_CODE, getLocale()))
                .thenReturn(validationExceptionMessage);
        when(messageSource.getMessage(ERROR_MESSAGE_CODE, new Object[]{}, ERROR_MESSAGE_CODE, getLocale())).thenReturn(ERROR_MESSAGE);

        final MethodArgumentNotValidException methodArgumentNotValidException =
                createExceptionWithFieldErrors(new FieldError(randomAlphabetic(10), errorPath, ERROR_MESSAGE_CODE));

        final ResponseEntity<Object> responseEntity = subject.handleMethodArgumentNotValid(methodArgumentNotValidException, null, null, null);

        assertThat(responseEntity, not(nullValue()));
        assertThat(responseEntity.getStatusCode(), is(UNPROCESSABLE_ENTITY));

        final Object responseBody = responseEntity.getBody();

        assertThat(responseBody, instanceOf(ErrorMessageResource.class));

        final ErrorMessageResource errorMessageResource = (ErrorMessageResource) responseBody;

        assertThat(errorMessageResource, not(nullValue()));
        assertThat(errorMessageResource.getMessage(), is(validationExceptionMessage));
        assertThat(errorMessageResource.getCode(), is("001-0500"));

        final List<ErrorItemResource> errorItemResources = errorMessageResource.getErrors();

        assertThat(errorItemResources, not(nullValue()));
        assertThat(errorItemResources.size(), is(1));

        final ErrorItemResource errorItemResource = errorItemResources.get(0);

        assertThat(errorItemResource.getPath(), is("test_path"));
        assertThat(errorItemResource.getMessage(), is(ERROR_MESSAGE));
    }

    @Test
    public void handleValidationException_ValidationException_UnprocessableEntityResponse() {
        final String errorPath = "path";
        final String validationExceptionMessage = "common-message";
        final ValidationException validationException = ValidationException.builder()
                .addValidationError(errorPath, ERROR_MESSAGE_CODE)
                .build();

        when(messageSource.getMessage(VALIDATION_EXCEPTION_MESSAGE_CODE, null, VALIDATION_EXCEPTION_MESSAGE_CODE, getLocale()))
                .thenReturn(validationExceptionMessage);
        when(messageSource.getMessage(ERROR_MESSAGE_CODE, new Object[]{}, ERROR_MESSAGE_CODE, getLocale())).thenReturn(ERROR_MESSAGE);

        final ResponseEntity<ErrorMessageResource> response = subject.handleValidationExceptions(validationException);

        assertThat(response, not(nullValue()));
        assertThat(response.getStatusCode(), is(UNPROCESSABLE_ENTITY));

        final Object responseBody = response.getBody();

        assertThat(responseBody, instanceOf(ErrorMessageResource.class));

        final ErrorMessageResource errorMessageResource = (ErrorMessageResource) responseBody;

        assertThat(errorMessageResource, not(nullValue()));
        assertThat(errorMessageResource.getMessage(), is(validationExceptionMessage));
        assertThat(errorMessageResource.getCode(), is(VALIDATION_EXCEPTION_CODE));

        final List<ErrorItemResource> errorItemResources = errorMessageResource.getErrors();

        assertThat(errorItemResources, not(nullValue()));
        assertThat(errorItemResources.size(), is(1));

        final ErrorItemResource errorItemResource = errorItemResources.get(0);

        assertThat(errorItemResource.getPath(), is(errorPath));
        assertThat(errorItemResource.getMessage(), is(ERROR_MESSAGE));
    }

    private MethodArgumentNotValidException createExceptionWithFieldErrors(FieldError... fieldErrors) throws NoSuchMethodException {
        final BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "object");

        for (final FieldError fieldError : fieldErrors) {
            bindingResult.addError(fieldError);
        }

        final MethodParameter methodParameter = new MethodParameter(TestClass.class.getMethod("testMethod", Object.class), 0);
        return new MethodArgumentNotValidException(methodParameter, bindingResult);
    }

    private class TestClass {
        public void testMethod(final Object object) {
            //Nothing
        }
    }
}
