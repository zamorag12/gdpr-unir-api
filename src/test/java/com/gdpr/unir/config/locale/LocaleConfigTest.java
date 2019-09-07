package com.gdpr.unir.config.locale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Locale;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;

@RunWith(MockitoJUnitRunner.class)
public class LocaleConfigTest {

    private static final Locale SPANISH = new Locale("es", "");

    private LocaleConfig subject;

    private MockHttpServletRequest mockHttpServletRequest;

    @Before
    public void setUp() {
        subject = new LocaleConfig();

        mockHttpServletRequest = new MockHttpServletRequest();
    }

    @Test
    public void resolveLocale_EnglishLocaleHeader_EnglishSetInLocale() {
        final String language = "en";
        mockHttpServletRequest.addHeader(ACCEPT_LANGUAGE, language);
        mockHttpServletRequest.setPreferredLocales(singletonList(new Locale(language)));

        final Locale locale = subject.localeResolver().resolveLocale(mockHttpServletRequest);

        assertThat(locale.getLanguage(), is(language));
    }

    @Test
    public void resolveLocale_SpanishLocaleHeader_SpanishSetInLocale() {
        mockHttpServletRequest.addHeader(ACCEPT_LANGUAGE, SPANISH);
        mockHttpServletRequest.setPreferredLocales(singletonList(SPANISH));

        final Locale locale = subject.localeResolver().resolveLocale(mockHttpServletRequest);

        assertThat(locale, is(SPANISH));
    }

    @Test
    public void resolveLocale_NoLocaleHeader_SpanishSetInLocale() {
        final Locale locale = subject.localeResolver().resolveLocale(mockHttpServletRequest);

        assertThat(locale, is(SPANISH));
    }

    @Test
    public void resolveLocale_UnknownLocaleHeader_SpanishSetInLocale() {
        final String language = "123";
        mockHttpServletRequest.addHeader(ACCEPT_LANGUAGE, language);
        mockHttpServletRequest.setPreferredLocales(singletonList(new Locale(language)));

        final Locale locale = subject.localeResolver().resolveLocale(mockHttpServletRequest);

        assertThat(locale, is(SPANISH));
    }
}