package com.capgemini.application.quarkus.sample.ping.service;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.*;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

//In Quarkus all JAX-RS resources are treated as CDI beans
//default is Singleton scope
@Path("/ping")
//Produces and Consumes on class level will be used for all methods unless overriden
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "ping")
public class PingRestController {

    //no @Path on this method = using path from class
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String ping() {
        return "Hello from Quarkus";
    }


    @APIResponses({
            @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(type = SchemaType.OBJECT, description =
                    "Health state as simple object"))),
            @APIResponse(responseCode = "500", description = "Server error")
    })
    @Operation(operationId = "getHealthInfo", description = "Returns a simple static health response")
    @GET//Invoke using HTTP GET
    @Path("pingResponse")//explicit method path
    //@consumers and @Produces is inherited from class
    public Response pingAsResponse() {
        //we construct our response payload
        Map<String, Object> data = new HashMap<>();
        data.put("state", "ok");
        data.put("healthy", true);
        // for advanced use cases, you can control the cache headers to instruct clients how to cache the resp
        CacheControl cc = new CacheControl();
        cc.setMaxAge(86400);// in this case. we tell the client to cache the resp for 1 day
        cc.setPrivate(true);
        //using response builder
        return Response
                //set status(using provided constants or simple int
                .status(Response.Status.OK)
                //entity is the actual payload we want to return/serialize
                .entity(data)
                //setting cache, there are many more things we can do
                //see available methods of Response for more info
                .cacheControl(cc)
                //adding custom response headers is simple as well
                .header("X-Hi", "Hi from quarkus")
                //ever heard of HATEOAS?
                .link("_google", "https://google.com")
                //builder to response
                .build();
    }


    @POST
    @Path("pingWithParam/{level}")//path with mandatory path param
    //Consumes/Provides is taken from class level
    public Response pingWithParams(
            //We must annotate a method param with @PathParam  matching name of our @Path segment
            @Parameter(description = "Level Parameter") @PathParam("level") String level,
            //Query params are optional ?info=value
            @QueryParam("info") String info,
            //Extract params from request headers
            @HeaderParam("Accept") String clientAccept,
            //un-annotated param is our payload
            Map<String, String> payload
    ) {
        Map<String, Object> data = new HashMap<>();
        data.put("pathParam", level);
        data.put("queryParam", info);
        data.put("headerParam", clientAccept);
        data.put("payload", payload);
        return Response.ok(data).build();
    }
}
