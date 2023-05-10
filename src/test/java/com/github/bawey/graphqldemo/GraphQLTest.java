package com.github.bawey.graphqldemo;

import com.github.bawey.graphqldemo.generated.server.languagesQuery.LanguagesQuery;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class GraphQLTest {
    private HttpServer server;
    private WebTarget target;

    @BeforeEach
    public void setUp() throws Exception {
        // start the server
        server = DemoServer.startServer();
        // create the client
        Client c = ClientBuilder.newClient();

        // uncomment the following line if you want to enable
        // support for JSON in the client (you also have to uncomment
        // dependency on jersey-media-json module in pom.xml and Main.startServer())
        // --
        // c.configuration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

        target = c.target(DemoServer.BASE_URI);
    }

    @AfterEach
    public void tearDown() throws Exception {
        server.shutdownNow();
    }

    @Test
    void testFetchingLanguages() {
        LanguagesQuery query = new LanguagesQuery();
        Entity<LanguagesQuery> entity = Entity.entity(query, MediaType.APPLICATION_JSON_TYPE);
        Response response = target.path("graphql").request("application/graphql").post(entity);
        System.out.println(response);
    }
}
