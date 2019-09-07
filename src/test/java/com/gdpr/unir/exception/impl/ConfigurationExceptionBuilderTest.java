package com.gdpr.unir.exception.impl;

import com.gdpr.unir.exception.ErrorMessage;
import org.junit.Test;

import java.util.Map;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ConfigurationExceptionBuilderTest {
    private static final String CONFIGURATION_EXCEPTION_CODE = "001-0600";

    @Test
    public void build_AllParametersGiven_ConfigurationException() {
        final String message = randomAlphabetic(10);
        final String errorMessageCode = randomAlphabetic(10);

        final Throwable cause = new RuntimeException();

        final String errorMessageArg0 = randomAlphabetic(10);
        final String errorMessageArg1 = randomAlphabetic(10);

        final String key0 = randomAlphabetic(10);
        final String key1 = randomAlphabetic(10);
        final String value0 = randomAlphabetic(10);
        final String value1 = randomAlphabetic(10);

        final ConfigurationException configurationException = ConfigurationException.builder()
                .message(message)
                .errorMessageKey(errorMessageCode)
                .cause(cause)
                .errorMessageArgs(errorMessageArg0, errorMessageArg1)
                .addAdditionalInformation(key0, value0)
                .addAdditionalInformation(key1, value1)
                .build();

        assertThat(configurationException, not(nullValue()));

        assertThat(configurationException.getMessage(), is(message));
        assertThat(configurationException.getCode(), is(CONFIGURATION_EXCEPTION_CODE));
        assertThat(configurationException.getCause(), is(cause));

        final ErrorMessage errorMessage = configurationException.getErrorMessage();

        assertThat(errorMessage, not(nullValue()));
        assertThat(errorMessage.getCode(), is(errorMessageCode));

        final Object[] errorMessageArgs = errorMessage.getArgs();

        assertThat(errorMessageArgs, not(nullValue()));
        assertThat(errorMessageArgs[0], is(errorMessageArg0));
        assertThat(errorMessageArgs[1], is(errorMessageArg1));

        final Map<String, Object> additionalInformation = configurationException.getAdditionalInformation();
        assertThat(additionalInformation.containsKey(key0), is(true));
        assertThat(additionalInformation.get(key0), is(value0));
        assertThat(additionalInformation.containsKey(key1), is(true));
        assertThat(additionalInformation.get(key1), is(value1));
    }
}
