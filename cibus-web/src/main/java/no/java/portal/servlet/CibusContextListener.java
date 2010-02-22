package no.java.portal.servlet;

import java.util.HashMap;
import java.util.Map;

import no.java.portal.page.JspBean;
import no.java.portal.domain.Category;
import no.java.portal.domain.PortalConfiguration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author <a href="mailto:trygvis@java.no">Trygve Laugst&oslash;l</a>
 * @version $Id$
 */
public class CibusContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
        WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(event.getServletContext());

        for (String name : applicationContext.getBeanNamesForType(JspBean.class)) {
            Object page = applicationContext.getBean(name);
            event.getServletContext().setAttribute(name, page);
        }
        PortalConfiguration configuration = (PortalConfiguration)
                applicationContext.getBean("portalConfiguration", PortalConfiguration.class);

        configuration.setContextPath(event.getServletContext().getContextPath());

        addEnumToContext(event, Category.class);
    }

    /**
     * Add category class to context so we may use it in an rtexpr
     *
     * @param event
     * @param enumClass
     */
	@SuppressWarnings("unchecked")
    private void addEnumToContext(ServletContextEvent event, Class<? extends Enum> enumClass) {
		final String enumClassName = enumClass.getSimpleName();
		final Enum[] enums = enumClass.getEnumConstants();
		Map<String, Enum> map = new HashMap<String, Enum>(enums.length);
		for (Enum anEnum : enums) {
			map.put(anEnum.name(), anEnum);
		}
		event.getServletContext().setAttribute(enumClassName, map);
	}

    public void contextDestroyed(ServletContextEvent sce) {
    }
}
