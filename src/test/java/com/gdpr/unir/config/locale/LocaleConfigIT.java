package com.gdpr.unir.config.locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;

import static java.util.Arrays.asList;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.US;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        LocaleConfig.class
})
@TestPropertySource(properties = {
        "spring.cloud.config.enabled=false",
        "aws.paramstore.enabled=false",
        "aws.secretsmanager.enabled=false"})
public class LocaleConfigIT {

    private static final Locale SPANISH = new Locale("es", "");
    private static final List<Locale> SUPPORTED_LOCALES = asList(SPANISH, ENGLISH, US);

    @Autowired
    private LocaleResolver localeResolver;

    @Test
    public void testBean() {
        assertThat(localeResolver, is(notNullValue()));
        assertThat(localeResolver, is(instanceOf(AcceptHeaderLocaleResolver.class)));

        final AcceptHeaderLocaleResolver acceptHeaderLocaleResolver = (AcceptHeaderLocaleResolver) localeResolver;

        assertThat(acceptHeaderLocaleResolver.getDefaultLocale(), is(SPANISH));
        assertThat(acceptHeaderLocaleResolver.getSupportedLocales(), is(SUPPORTED_LOCALES));
    }
}