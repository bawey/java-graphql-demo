package com.github.bawey.graphqldemo;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphQLContext {
    @Bean
    public HelloResource helloResource() {
        return new HelloResource();
    }
}
