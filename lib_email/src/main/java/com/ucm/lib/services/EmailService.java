package com.ucm.lib.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.regex.Pattern;

/**
 * Simple service for sending e-mail
 * @author Joshua Podhola
 */
@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine htmlTemplateEngine;
    private final TemplateEngine textTemplateEngine;
    private final TemplateEngine stringTemplateEngine;

    public EmailService(JavaMailSender mailSender,
                        @Qualifier("emailTemplateEngine") TemplateEngine htmlTemplateEngine,
                        @Qualifier("emailTemplateEngine") TemplateEngine textTemplateEngine,
                        @Qualifier("emailTemplateEngine") TemplateEngine stringTemplateEngine) {
        this.mailSender = mailSender;
        this.htmlTemplateEngine = htmlTemplateEngine;
        this.textTemplateEngine = textTemplateEngine;
        this.stringTemplateEngine = stringTemplateEngine;
    }

    /**
     * Format and send an email.
     * Formatting:
     *  /html/[NAME] for HTML templates. Should be found in resources/mail/html/[NAME].html
     *  /text/[NAME] for plain text templates. Should be found in resources/mail/text/[NAME].txt
     *  Any string not matching either of these patterns will be treated as an HTML5 string.
     * A Thymeleaf context is required, even if no variables need to be passed to the template.
     * To construct a new context:
     *  final Context ctx = new Context(locale);
     * Locale should be of type java.util.Locale. Pass new Locale("en") for English, or some other value.
     * For each variable:
     *  ctx.setVariable("key", value);
     * @author Joshua Podhola
     * @param recipientEmail Recipient's Email Address
     * @param ctx org.thymeleaf.context.Context.
     * @param template Template to parse.
     * @param subject Email subject.
     * @throws MessagingException Unable to send email (likely failed to connect to SMTP server).
     */
    public void sendEmail(final String recipientEmail, final Context ctx, final String template,
                          final String subject) throws MessagingException {
        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setSubject(subject);
        message.setFrom("noreply@cashmoney.com");
        message.setTo(recipientEmail);

        // Choose the right template engine. Might be a better way to do this, but I'm still new to Thymeleaf. -JP
        Pattern html = Pattern.compile("^html/", Pattern.CASE_INSENSITIVE);
        Pattern text = Pattern.compile("^text/", Pattern.CASE_INSENSITIVE);
        if(html.matcher(template).matches()) {
            final String htmlContent = this.htmlTemplateEngine.process(template, ctx);
            message.setText(htmlContent, true);
        }
        else if(text.matcher(template).matches()) {
            final String textContent = this.textTemplateEngine.process(template, ctx);
            message.setText(textContent);
        }
        else {
            final String textContent = this.stringTemplateEngine.process(template, ctx);
            message.setText(textContent, true);
        }

        this.mailSender.send(mimeMessage);
    }
}
