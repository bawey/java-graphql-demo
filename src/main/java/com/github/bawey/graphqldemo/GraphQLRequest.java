package com.github.bawey.graphqldemo;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class GraphQLRequest {
    private final String document;
    private final String operationName;
    private final Map<String, Object> arguments;
}
