package com.gdpr.unir.scheduler;

import com.gdpr.unir.mail.MailService;
import com.gdpr.unir.users.model.User;
import com.gdpr.unir.users.repository.UserRepository;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.gdpr.unir.users.model.User.UserStatus.CONFIRMED;
import static java.lang.Long.MAX_VALUE;
import static java.util.stream.Collectors.toList;
import static org.apache.tomcat.util.file.ConfigFileLoader.getInputStream;

@Slf4j
@Component
public class ScheduleTasks {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final String emailTemplatePath = "classpath:mail-template/email.html";

    private final MailService mailService;
    private final UserRepository userRepository;

    public ScheduleTasks(final MailService mailService, final UserRepository userRepository) {
        this.mailService = mailService;
        this.userRepository = userRepository;
    }

    @Scheduled(initialDelay = 1000 * 30, fixedDelay = MAX_VALUE)
    public void scheduleTaskToSendRandomEmails() throws IOException {
        log.info("Fixed Rate Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));

        final List<User> users = (List<User>) userRepository.findAll();

        final List<User> confirmedUsers = users.stream()
                .filter(user -> user.getStatus().equals(CONFIRMED))
                .collect(toList());

        for (final User user : confirmedUsers) {
            final InputStream emailTemplateFile = getInputStream(emailTemplatePath);
            final StringBuilder contentBuilder = new StringBuilder();

            try {
                final BufferedReader in = new BufferedReader(new InputStreamReader(emailTemplateFile));
                String str;
                while ((str = in.readLine()) != null) {
                    contentBuilder.append(str);
                }
                in.close();
            } catch (IOException e) {
            }

            final String content = contentBuilder.toString();

            final Email email = new Email(user.getUserEmail());

            if (email != null) {
                mailService.sendEmail(content, new Email(user.getUserEmail()));
            }
        }

    }
}
