package com.devonfw.demoquarkus.rest.v1.controller;

import org.eclipse.microprofile.jwt.JsonWebToken;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@RequestScoped
@Path("/secure")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SecureRestController {
    
    @Inject
    JsonWebToken jwt;
    
    @GET()
    @Path("/permit")
    @PermitAll 
    @Produces(MediaType.TEXT_PLAIN)
    public String permit() {
        return "Access to /secure/permit was granted!";
    }
    
    @GET()
    @Path("/token")
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public String token(@Context SecurityContext ctx) {
        String name;
        if (ctx.getUserPrincipal() == null) { 
            name = "anonymous";
        } else if (!ctx.getUserPrincipal().getName().equals(jwt.getName())) { 
            throw new InternalServerErrorException("Principal and JsonWebToken names do not match");
        } else {
            name = ctx.getUserPrincipal().getName(); 
        }
        
        StringBuilder s = new StringBuilder();
        if(jwt != null && ctx != null) {
        	s.append(String.format("hello + %s,"
                    + " isHttps: %s,"
                    + " authScheme: %s,",
                    name, ctx.isSecure(), ctx.getAuthenticationScheme()));
            s.append(String.format("issuer: %s\n"
            		+ "tokenID: %s\n"
            		+ "rawToken: %s",
            		jwt.getIssuer(), jwt.getTokenID(), jwt.getRawToken()));
            if(jwt.getClaimNames() != null) {
            	for(String claim : jwt.getClaimNames()) {
                	s.append(claim + ": " + jwt.getClaim(claim).toString() + "\n");
                }
            }
        }
        return s.toString(); 
    }
    
    @GET
    @Path("/roles-allowed/admin") 
    @RolesAllowed({ "admin" }) 
    @Produces(MediaType.TEXT_PLAIN)
    public String rolesAllowedAdmin() {
        return "Access granted for user with role 'admin'";
    }
    
    @GET
    @Path("/roles-allowed/user") 
    @RolesAllowed({ "user" }) 
    @Produces(MediaType.TEXT_PLAIN)
    public String rolesAllowedUser() {
        return "Access granted for user with role 'user'";
    }
    
    @GET
    @Path("/roles-allowed/my-role") 
    @RolesAllowed({ "myrole" }) 
    @Produces(MediaType.TEXT_PLAIN)
    public String rolesAllowedMyRole() {
        return "Access granted for user with role 'myrole'";
    }

}
