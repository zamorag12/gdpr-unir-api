package com.gdpr.unir.mail;

import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.sendgrid.Method.POST;
import static org.springframework.util.MimeTypeUtils.TEXT_HTML_VALUE;

@Slf4j
@Service
public class MailService {
    private static final String EMAIL_PROVIDER_ACTION = "mail/send";

    private final SendGrid sendGrid;

    public MailService(final SendGrid sendGrid) {
        this.sendGrid = sendGrid;
    }

    public void sendEmail(final String fileString, final Email emailTo) throws IOException {
        final Email from = new Email("no-reply@unir-tfm-gzamora2019.com");

        final Content content = new Content(TEXT_HTML_VALUE, fileString);

        final Personalization personalization = new Personalization();
        personalization.addTo(emailTo);

        final Mail mail = new Mail();
        mail.setFrom(from);
        mail.setSubject("test");
        mail.addContent(content);
        mail.addPersonalization(personalization);

        final Request request = new Request();
        request.setMethod(POST);
        request.setEndpoint(EMAIL_PROVIDER_ACTION);
        request.setBody(mail.build());

        final Response response = sendGrid.api(request);

        if (response.getStatusCode() == 202) {
            log.info("Email sent with success to: " + emailTo.getEmail());
        }
    }
}
