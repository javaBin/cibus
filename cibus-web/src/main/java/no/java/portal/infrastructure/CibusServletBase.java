package no.java.portal.infrastructure;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Base for {@link JavaBinICSServlet} and {@link JavaBinRSSServlet}
 *
 * @author Thor Åge Eldby
 * @since 20. mai. 2008
 */
public class CibusServletBase extends HttpServlet {

    private static final long serialVersionUID = 4587542913523625791L;

    private static final String CONFIG_FILE = "/opt/jb/lookicanical/etc/config.properties";

	static String address = "http://www4.java.no/web/modules/javabin/rest/category-get.jsp";

    public <T> T getBean(String id, Class<T> klass) {
        return klass.cast(WebApplicationContextUtils
            .getRequiredWebApplicationContext(getServletContext()).getBean(id, klass));
    }

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		File config = new File(CONFIG_FILE);
		if (config.canRead()) {
			servletConfig.getServletContext().log(
					"Reading configuration file: " + CONFIG_FILE);
			Properties properties = loadProperties(config);
			address = properties.getProperty("address");
			servletConfig.getServletContext().log("Address: " + address);
		} else {
			servletConfig.getServletContext().log(
					"No config file available, using defaults.");
		}
	}

	/**
	 * @param config
	 * @return
	 * @throws ServletException
	 */
	private Properties loadProperties(File config) {
		FileInputStream is = null;
		try {
			Properties properties = new Properties();
			is = new FileInputStream(config);
			properties.load(is);
			return properties;
		} catch (IOException e) {
			throw new RuntimeException(
					"Error while reading configuration file", e);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				// ignore
			}
		}
	}

	public String getAddress() {
		return address;
	}

}
