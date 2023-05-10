package com.github.bawey.graphqldemo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public class GraphQLRequest {
    private final String query;
    private final String operationName;
    private final Map<String, Object> variables;
}
