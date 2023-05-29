package com.github.bawey.graphqldemo;

import com.github.bawey.graphqldemo.config.JacksonObjectMapperProvider;
import com.github.bawey.graphqldemo.disposable.GraphQLResponse;
import com.github.bawey.graphqldemo.generated.client.languagesQuery.LanguagesQuery;
import com.github.bawey.graphqldemo.generated.client.languagesQuery.LanguagesQueryResult;
import com.github.bawey.graphqldemo.generated.client.languagesQuery.languages.LanguageResult;
import com.github.bawey.graphqldemo.generated.client.lexemesQuery.LexemesQuery;
import com.github.bawey.graphqldemo.generated.client.lexemesQuery.LexemesQueryResult;
import com.github.bawey.graphqldemo.generated.client.lexemesQuery.lexemes.LexemeResult;
import com.github.bawey.graphqldemo.generated.client.types.LexemesFilter;
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
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class DemoServerTest {

    private HttpServer server;
    private WebTarget target;

    @BeforeEach
    public void setUp() {
        // start the server
        server = DemoServer.startServer();
        // create the client
        Client c = ClientBuilder.newBuilder().register(JacksonObjectMapperProvider.class).register(JacksonFeature.class).build();

        target = c.target(DemoServer.BASE_URI);
    }

    @AfterEach
    public void tearDown() {
        server.shutdownNow();
    }

    @Test
    void testFetchingLanguages() {
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
        assertThat(gqlResponse.getData().getLanguages().stream().map(LanguageResult::getShortIsoCode)
                .map(String::toLowerCase).collect(Collectors.toSet()))
                .containsExactlyInAnyOrder("fr", "en", "de");
    }

    @Test
    void testFetchingAllLexemes() {
        // given
        LexemesQuery query = new LexemesQuery(lexemesQueryVariables -> {
        });
        GraphQLRequest request = GraphQLRequest.builder().query(query.getDocument())
                .variables(query.getVariables()).operationName(query.getOperationName()).build();

        //when
        GraphQLResponse<LexemesQueryResult> gqlResponse;
        try (Response response = target.path("graphql").request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE))) {
            // read a generic type from response
            gqlResponse = response.readEntity(new GenericType<>() {
            });
        }

        //then
        assertThat(gqlResponse.areErrorsPresent()).isFalse();
        assertThat(gqlResponse.getData().getLexemes().stream().map(e -> e.getLanguage().getName()).sorted().distinct()
                .collect(Collectors.toList())).containsExactly("English", "French", "German");
        assertThat(gqlResponse.getData().getLexemes().stream().map(LexemeResult::getHeadword).sorted().distinct()
                .collect(Collectors.toList())).containsAll(List.of("chat", "dog", "cat", "pitch"));
    }

    @Test
    void testFetchingFilteredLexemes() {
        // given
        LexemesQuery query = new LexemesQuery(lexemesQueryVariables -> lexemesQueryVariables
                .setFilters(List.of(
                        LexemesFilter.builder().setToken("cha").build(),
                        LexemesFilter.builder().setToken("dog").setLanguageCode("EN").build()
                )));
        GraphQLRequest request = GraphQLRequest.builder().query(query.getDocument())
                .variables(query.getVariables()).operationName(query.getOperationName()).build();

        //when
        GraphQLResponse<LexemesQueryResult> gqlResponse;
        try (Response response = target.path("graphql").request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE))) {
            // read a generic type from response
            gqlResponse = response.readEntity(new GenericType<>() {
            });
        }

        //then
        assertThat(gqlResponse.areErrorsPresent()).isFalse();
        assertThat(gqlResponse.getData().getLexemes().stream().map(e -> e.getLanguage().getName()).sorted().distinct()
                .collect(Collectors.toList())).containsExactly("English", "French");
        assertThat(gqlResponse.getData().getLexemes().stream().map(LexemeResult::getHeadword).sorted().distinct()
                .collect(Collectors.toList())).containsExactly("chat", "dog");
    }
}
