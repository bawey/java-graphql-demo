package com.github.bawey.graphqldemo.config;

import com.github.bawey.graphqldemo.generated.server.types.NounProperties;
import com.github.bawey.graphqldemo.generated.server.types.VerbProperties;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.TypeResolver;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class GraphQLTypeResolver implements TypeResolver {

    @Override
    public GraphQLObjectType getType(TypeResolutionEnvironment env) {
        Object javaObject = env.getObject();
        return Stream.of(VerbProperties.class, NounProperties.class).filter(clazz -> clazz.isInstance(javaObject))
                .map(clazz -> env.getSchema().getObjectType(clazz.getSimpleName())).findFirst().orElseThrow();
    }
}
