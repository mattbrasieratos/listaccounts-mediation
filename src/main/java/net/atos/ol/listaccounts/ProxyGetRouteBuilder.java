package net.atos.ol.listaccounts;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.Exchange;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;


@ApplicationScoped
@ContextName("camel-cdi-context")
public class ProxyGetRouteBuilder
        extends RouteBuilder
{
    String host = "ol001-listaccounts-stub";
    String port = "8080";
    String context = "/ol001-listaccounts-stub";

    public ProxyGetRouteBuilder() {}

    public void configure() throws Exception
    {
        try {
            Properties p = new Properties();
            FileInputStream file = new FileInputStream("/proxy.properties");
            p.load(file);
            if (p.get("host") != null) {
                host = (String) p.get("host");
            }
            if (p.get("port") != null) {
                port = (String) p.get("port");
            }
            if (p.get("context") != null) {
                context = (String) p.get("context");
            }
        }
        catch (FileNotFoundException fnfe)
        {
            log.warn("Unable to open /proxy.properties. Defaulting proxy settings");
        }
        from("direct:listaccounts-v1")
                .routeId("listaccounts-mediation")
                .errorHandler(loggingErrorHandler("listaccounts-mediation").level(LoggingLevel.ERROR))
                .log(simple("v1:Received request for ${header.request-path}").getText())
                .setHeader(Exchange.HTTP_URI,
                            simple("http4://" + host + ":" + port + context + "${header.request-path}"))
                .to("http4://dummy:12345?throwExceptionOnFailure=false") //URI here is overridden using header above
                .log(simple("v1:Received response from http://" + host + ":" + port + context + "${header.request-path}").getText())
                .convertBodyTo(java.lang.String.class);


        from("direct:listaccounts-default")
                .log(simple("default:Received request for ${header.request-path}").getText())
                .log(simple("default:Decision version select: route to v1").getText())
                .to("direct:listaccounts-v1");
    }
}
