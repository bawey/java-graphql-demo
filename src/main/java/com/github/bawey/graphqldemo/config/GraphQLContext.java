package com.github.bawey.graphqldemo.config;


import com.github.bawey.graphqldemo.DemoServer;
import com.github.bawey.graphqldemo.fetchers.LanguageDataFetcher;
import com.github.bawey.graphqldemo.fetchers.LanguagesDataFetcher;
import com.github.bawey.graphqldemo.fetchers.LexemesDataFetcher;
import graphql.GraphQL;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentation;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentationOptions;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
@ComponentScan(basePackageClasses = DemoServer.class)
public class GraphQLContext {

    @Bean
    public GraphQL graphQL(GraphQLSchema graphQLSchema, DataLoaderDispatcherInstrumentation dataLoaderDispatcherInstrumentation) {
        return GraphQL.newGraphQL(graphQLSchema).instrumentation(dataLoaderDispatcherInstrumentation).build();
    }

    @Bean
    public DataLoaderDispatcherInstrumentation dataLoaderDispatcherInstrumentation() {
        return new DataLoaderDispatcherInstrumentation(DataLoaderDispatcherInstrumentationOptions.newOptions());
    }

    @Bean
    GraphQLSchema graphQLSchema(TypeDefinitionRegistry typeDefinitionRegistry, RuntimeWiring runtimeWiring) {
        return new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
    }

    @Bean
    public TypeDefinitionRegistry typeDefinitionRegistry() throws IOException {
        return new SchemaParser().parse(new ClassPathResource("graphql/schema.graphqls").getInputStream());
    }

    @Bean
    public RuntimeWiring runtimeWiring(LanguagesDataFetcher languagesDataFetcher, LexemesDataFetcher lexemesDataFetcher, LanguageDataFetcher languageDataFetcher) {
        RuntimeWiring.Builder wiringBuilder = RuntimeWiring.newRuntimeWiring();

        wiringBuilder.type("Query", builder -> builder.dataFetcher("languages", languagesDataFetcher));
        wiringBuilder.type("Query", builder -> builder.dataFetcher("lexemes", lexemesDataFetcher));
        wiringBuilder.type("Lexeme", builder -> builder.dataFetcher("language", languageDataFetcher));
        return wiringBuilder.build();
    }


}
