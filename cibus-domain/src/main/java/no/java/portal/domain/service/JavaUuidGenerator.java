package no.java.portal.domain.service;

import org.springframework.stereotype.*;

import java.util.*;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
@Component
public class JavaUuidGenerator implements UuidGenerator {
    public UUID get() {
        return UUID.randomUUID();
    }
}
