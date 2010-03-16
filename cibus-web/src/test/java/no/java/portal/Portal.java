package no.java.portal;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.resource.ResourceCollection;

/**
 * @author <a href="mailto:kaare.nilsen@arktekk.no">Kaare Nilsen (kni)</a>
 */
public class Portal {

    public String constrettoTags() {
        return "dev";
    }

    public String constrettoEnv() {
        return "";
    }

    public Portal()  {
        System.setProperty("CONSTRETTO_TAGS", constrettoTags());
        System.setProperty("CONSTRETTO_ENV", constrettoEnv());

        final Server server = new Server();
        final Connector connector = new SelectChannelConnector();
        connector.setPort(8080);
        server.setConnectors(new Connector[]{connector});
        final WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        ResourceCollection paths = new ResourceCollection(new String[]{TestHelper.getWebAppDir(this.getClass())});
        context.setBaseResource(paths);
        server.addHandler(context);

        try {
            server.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new Portal();
    }

}
