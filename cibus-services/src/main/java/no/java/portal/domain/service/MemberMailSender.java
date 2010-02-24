package no.java.portal.domain.service;

import no.java.portal.domain.member.*;

import java.io.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public interface MemberMailSender {
    void sendNewAccountEmail(Member member) throws IOException;

    void sendResetPasswordEmail(Member member);
}
