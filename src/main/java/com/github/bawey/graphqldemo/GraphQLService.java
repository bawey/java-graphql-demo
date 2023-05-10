package com.github.bawey.graphqldemo;


import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GraphQLService {
    private final GraphQL graphQL;

    public ExecutionResult execute(GraphQLRequest graphQLRequest) {
        ExecutionInput.Builder inputBuilder = ExecutionInput.newExecutionInput();
        Optional.ofNullable(graphQLRequest.getOperationName()).ifPresent(inputBuilder::operationName);
        Optional.ofNullable(graphQLRequest.getVariables()).ifPresent(inputBuilder::variables);
        inputBuilder.query(graphQLRequest.getQuery());
        return graphQL.execute(inputBuilder.build());
    }
}
