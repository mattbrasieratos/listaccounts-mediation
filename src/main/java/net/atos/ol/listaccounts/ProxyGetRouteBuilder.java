package net.atos.ol.listaccounts;

        import javax.enterprise.context.ApplicationScoped;
        import javax.inject.Inject;
        import javax.servlet.http.HttpServletRequest;

        import org.apache.camel.Endpoint;
        import org.apache.camel.LoggingLevel;
        import org.apache.camel.Processor;
        import org.apache.camel.builder.RouteBuilder;
        import org.apache.camel.cdi.ContextName;
        import org.apache.camel.Exchange;
        import org.apache.camel.cdi.Uri;

        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.IOException;
        import java.util.Properties;


@ApplicationScoped
@ContextName("camel-cdi-context")
public class ProxyGetRouteBuilder
        extends RouteBuilder
{
    private static final String V1 = "v1/";
    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static String API_CONTEXT = "account/";
    private static String BACK_END_CONTEXT = "b2crestapi/";
    private static String API_OPERATION = "/listaccounts";
    private static String BACK_END_OPERATION = "/principaltocashaccountlinks";
    String host = "ol001-listaccounts-stub";
    String port = "8080";
    String context = "/ol001-listaccounts-stub/";


    @Inject
    @Uri("jetty:http://0.0.0.0:1080?matchOnUriPrefix=true")
    private Endpoint jettyEndpoint;



    public ProxyGetRouteBuilder() {}

    public void configure() throws Exception
    {
        readProperties();
        from("direct:listaccounts-v1")
                .routeId("listaccounts-mediation-v1")
                .errorHandler(loggingErrorHandler("listaccounts-mediation").level(LoggingLevel.ERROR))
                .setHeader("request-path",simple("${header.CamelHttpPath}"))
                .log(simple("v1:Received request for ${header.request-path} from ${header.request-ip} forward for ${header.forward-for}").getText())
                .setHeader(Exchange.HTTP_URI,
                        simple("http4://" + host + ":" + port))
                //In this instance we set the path to the incoming path. We might want to translate for other services
                .setHeader(Exchange.HTTP_PATH, simple(context+"${header.request-path}"))
                .to("http4://listaccounts:12345?throwExceptionOnFailure=false") //URI here is overridden using header above
                .log(simple("v1:Received response from http://" + host + ":" + port +"${header.CamelHttpPath}").getText())
                .convertBodyTo(java.lang.String.class);

        from(jettyEndpoint)
                .routeId("listaccounts-mediation")
                .setHeader("request-path",simple("${header.CamelHttpPath}"))
                .log(simple("version-select:Received request for ${header.request-path}").getText())
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        //Set the version header
                        exchange.getOut().setHeaders(exchange.getIn().getHeaders());
                        exchange.getOut().setBody(exchange.getIn().getBody());
                        String requestPath = (String)exchange.getIn().getHeader(Exchange.HTTP_PATH);
                        if (requestPath.startsWith(V1))
                        {
                            requestPath = requestPath.substring(requestPath.indexOf(V1)+V1.length(),requestPath.length());
                            exchange.getOut().setHeader("service-version","1");
                            exchange.getOut().setHeader(Exchange.HTTP_PATH,requestPath);
                        }
                    }
                })
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        //populate the request-ip and forward-ip headers
                        exchange.getOut().setHeaders(exchange.getIn().getHeaders());
                        exchange.getOut().setBody(exchange.getIn().getBody());
                        HttpServletRequest req = exchange.getIn().getBody(HttpServletRequest.class);
                        String forwardFor = req.getHeader(X_FORWARDED_FOR);
                        String remoteAddr = req.getRemoteAddr();
                        int remotePort = req.getRemotePort();
                        exchange.getOut().setHeader("request-ip", remoteAddr + ":" + remotePort);
                        exchange.getOut().setHeader("forward-for", forwardFor);
                    }
                })
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        //perform URI transformation from the URI we expose to the back end URI
                        exchange.getOut().setHeaders(exchange.getIn().getHeaders());
                        exchange.getOut().setBody(exchange.getIn().getBody());
                        String requestPath = (String)exchange.getIn().getHeader(Exchange.HTTP_PATH);
                        requestPath = requestPath.replace(API_CONTEXT, BACK_END_CONTEXT);
                        requestPath = requestPath.replace(API_OPERATION, BACK_END_OPERATION);
                        exchange.getOut().setHeader(Exchange.HTTP_PATH,requestPath);

                    }
                })

                .choice()
                .when(header("service-version").isEqualTo("1"))
                .log(simple("version-select:Decision version select: route to v1").getText())
                .to("direct:listaccounts-v1")
                .otherwise()
                .log(simple("version-select:Decision version select: default to v1").getText())
                .to("direct:listaccounts-v1")
                .endChoice();
    }

    private void readProperties() throws IOException {
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
    }
}

