package com.github.bawey.graphqldemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.bawey.graphqldemo.config.JacksonObjectMapperProvider;
import com.github.bawey.graphqldemo.disposable.GraphQLResponse;
import com.github.bawey.graphqldemo.generated.server.languagesQuery.LanguagesQuery;
import com.github.bawey.graphqldemo.generated.server.languagesQuery.LanguagesQueryResult;
import com.github.bawey.graphqldemo.generated.server.languagesQuery.languages.LanguageResult;
import com.github.bawey.graphqldemo.resources.GraphQLRequest;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class DemoServerTest {

    private HttpServer server;
    private WebTarget target;

    @BeforeEach
    public void setUp() throws Exception {
        // start the server
        server = DemoServer.startServer();
        // create the client
        Client c = ClientBuilder.newBuilder().register(JacksonObjectMapperProvider.class).register(JacksonFeature.class).build();

        target = c.target(DemoServer.BASE_URI);
    }

    @AfterEach
    public void tearDown() throws Exception {
        server.shutdownNow();
    }

    @Test
    void testFetchingLanguages() throws JsonProcessingException {
        // create a query - happens to be a simple one
        LanguagesQuery query = new LanguagesQuery();
        GraphQLRequest request = GraphQLRequest.builder()
                .operationName(query.getOperationName())
                .query(query.getDocument())
                .variables(query.getVariables()).build();

        // wrap in an entity and post it away
        GraphQLResponse<LanguagesQueryResult> gqlResponse;
        try (Response response = target.path("graphql").request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE))) {
            // read a generic type from response
            gqlResponse = response.readEntity(new GenericType<>() {
            });
        }

        // make sure all is peachy and that we receive the expected languages
        assertThat(gqlResponse.areErrorsPresent()).isFalse();
        assertThat(gqlResponse.isDataPresent()).isTrue();
        assertThat(gqlResponse.getData().getLanguages().stream().map(LanguageResult::getShortIsoCode).map(String::toLowerCase).collect(Collectors.toSet())).containsExactlyInAnyOrder("fr", "en", "de");

    }
}
