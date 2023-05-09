package com.github.bawey.graphqldemo;


import graphql.ExecutionResult;
import graphql.GraphQL;
import lombok.RequiredArgsConstructor;

import javax.ws.rs.ext.Provider;

@RequiredArgsConstructor
@Provider
public class GraphQLService {
    private final GraphQL graphQL;

    public ExecutionResult execute(GraphQLRequest graphQLRequest) {
        return ExecutionResult.newExecutionResult().build();
    }
}
