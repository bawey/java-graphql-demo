package com.github.bawey.graphqldemo.resources;

import com.github.bawey.graphqldemo.services.GraphQLService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@RequiredArgsConstructor
@Resource
@Controller
@Path("graphql")
public class GraphQLResource {

    private final GraphQLService graphQLService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON, "application/graphql"})
    public Response performGraphQLOperation(GraphQLRequest request) {
        return Response.ok(graphQLService.execute(request)).build();
    }
}
