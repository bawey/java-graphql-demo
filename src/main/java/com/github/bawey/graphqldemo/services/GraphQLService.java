package com.github.bawey.graphqldemo.services;


import com.github.bawey.graphqldemo.loaders.LanguagesBatchLoader;
import com.github.bawey.graphqldemo.resources.GraphQLRequest;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import lombok.RequiredArgsConstructor;
import org.dataloader.DataLoaderFactory;
import org.dataloader.DataLoaderRegistry;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GraphQLService {
    private final GraphQL graphQL;
    private final LanguagesBatchLoader languagesBatchLoader;

    public ExecutionResult execute(GraphQLRequest graphQLRequest) {

        DataLoaderRegistry dataLoaderRegistry = new DataLoaderRegistry();
        dataLoaderRegistry.register(LanguagesBatchLoader.class.getSimpleName(),
                DataLoaderFactory.newMappedDataLoader(languagesBatchLoader));

        ExecutionInput.Builder inputBuilder = ExecutionInput.newExecutionInput();
        Optional.ofNullable(graphQLRequest.getOperationName()).ifPresent(inputBuilder::operationName);
        Optional.ofNullable(graphQLRequest.getVariables()).ifPresent(inputBuilder::variables);
        inputBuilder.query(graphQLRequest.getQuery());
        inputBuilder.dataLoaderRegistry(dataLoaderRegistry);
        return graphQL.execute(inputBuilder.build());
    }
}
