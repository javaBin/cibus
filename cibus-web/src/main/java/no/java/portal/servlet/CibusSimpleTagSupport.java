package no.java.portal.servlet;

import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * @author <a href="mailto:trygve.laugstol@arktekk.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public abstract class CibusSimpleTagSupport extends SimpleTagSupport {

    public <T> T getBean(String id, Class<T> klass) {
        return klass.cast(WebApplicationContextUtils
            .getRequiredWebApplicationContext(((PageContext) getJspContext())
                .getServletContext()).getBean(id, klass));
    }
}
