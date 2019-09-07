package com.gdpr.unir.config.locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;

import static java.util.Arrays.asList;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.US;

@Configuration
public class LocaleConfig {
    private static final Locale SPANISH = new Locale("es", "");
    private static final List<Locale> SUPPORTED_LOCALES = asList(SPANISH, ENGLISH, US);

    @Bean
    public LocaleResolver localeResolver() {
        final AcceptHeaderLocaleResolver acceptHeaderLocaleResolver = new AcceptHeaderLocaleResolver();
        acceptHeaderLocaleResolver.setDefaultLocale(SPANISH);
        acceptHeaderLocaleResolver.setSupportedLocales(SUPPORTED_LOCALES);
        return acceptHeaderLocaleResolver;
    }
}