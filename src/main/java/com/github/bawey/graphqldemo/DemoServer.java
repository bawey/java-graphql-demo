package com.github.bawey.graphqldemo;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;


public class DemoServer {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/";


    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     *
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.github.bawey.graphqldemo package
        final ResourceConfig rc = new ResourceConfig();
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(GraphQLContext.class);
        rc.property("contextConfig", context);
        // Possibly a break-around to have Jersey see the resources
        context.getBeansWithAnnotation(Resource.class).values().forEach(rc::register);
        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI

        rc.packages("org.glassfish.jersey.examples.jackson").register(JacksonObjectMapperProvider.class).register(JacksonFeature.class);

        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.printf("Jersey app started with endpoints available at " + "%s%nHit Ctrl-C to stop it...%n", BASE_URI);
        System.in.read();
        server.stop();
    }
}

