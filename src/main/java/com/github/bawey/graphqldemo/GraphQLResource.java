package com.github.bawey.graphqldemo;

import lombok.RequiredArgsConstructor;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@RequiredArgsConstructor
@Path("graphql")
@Resource
public class GraphQLResource {

    private final GraphQLService graphQLService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON, "application/graphql"})
    public Response performGraphQLOperation(GraphQLRequest request) {
        return Response.ok(graphQLService.execute(request)).build();
    }
}
