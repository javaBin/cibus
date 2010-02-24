package no.java.portal.domain.service;

import no.java.portal.domain.member.*;
import no.java.portal.domain.member.Member.*;
import org.apache.commons.io.*;
import org.apache.log4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.text.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
@Component
public class DefaultMemberMailSender implements MemberMailSender {

    private Logger logger = Logger.getLogger("MAIL");

    private final JavaMailSender mailSender;
    private final MimeMessageHelper newAccountTemplate;
    private final String newAccountText;
    private final MimeMessageHelper resetPasswordTemplate;
    private final String resetPasswordText;

    @Autowired
    public DefaultMemberMailSender(JavaMailSender mailSender,
                                   @Qualifier("newAccountTemplate") MimeMessageHelper newAccountTemplate,
                                   @Qualifier("resetPasswordTemplate") MimeMessageHelper resetPasswordTemplate) throws IOException {
        this.mailSender = mailSender;
        this.newAccountTemplate = newAccountTemplate;
        this.resetPasswordTemplate = resetPasswordTemplate;

        newAccountText = IOUtils.toString(getClass().getResourceAsStream("/mail/new-account.txt"));
        resetPasswordText = IOUtils.toString(getClass().getResourceAsStream("/mail/reset-password.txt"));
    }

    public void sendNewAccountEmail(Member member) throws IOException {
        MimeMailMessage mailMsg = new MimeMailMessage(newAccountTemplate);
        String message = MessageFormat.format(newAccountText, member.no, member.firstName, member.lastName);
        EmailAddress recipient = member.getPrimaryEmail();

        logger.debug("Sending new account email to " + member.no + "/" + recipient);
        mailMsg.setText(message);
        mailMsg.setTo(recipient.toString());
        mailSender.send(mailMsg.getMimeMessage());
    }

    public void sendResetPasswordEmail(Member member) {
    }
}
