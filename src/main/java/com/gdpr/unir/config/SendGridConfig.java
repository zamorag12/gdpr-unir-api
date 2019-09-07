package com.gdpr.unir.config;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendGridConfig {

    @Value("${sendGrid.apiKey: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}")
    private String sendGridApiKey;

    @Bean
    public SendGrid sendGrid() {
        return new SendGrid(sendGridApiKey);
    }
}
