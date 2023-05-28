package com.github.bawey.graphqldemo.resources;

import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class GraphQLRequest {
    private String query;
    private String operationName;
    private Map<String, Object> variables;
}
