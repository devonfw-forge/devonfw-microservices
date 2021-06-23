package com.devonfw.demoquarkus.rest.v1.controller;

import com.devonfw.demoquarkus.rest.v1.mapper.AnimalMapper;
import com.devonfw.demoquarkus.domain.model.Animal;
import com.devonfw.demoquarkus.rest.v1.model.AnimalDTO;
import com.devonfw.demoquarkus.rest.v1.model.AnimalSearchCriteriaDTO;
import com.devonfw.demoquarkus.rest.v1.model.NewAnimalDTO;
import com.devonfw.demoquarkus.ApplicationAccessControlConfig;
import com.devonfw.demoquarkus.domain.dao.AnimalDAO;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.tkit.quarkus.jpa.daos.PageResult;
import org.tkit.quarkus.rs.models.PageResultDTO;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Random;

@RequestScoped
//In Quarkus all JAX-RS resources are treated as CDI beans
//default is Singleton scope
@Path("/animals")
// how we serialize response
@Produces(MediaType.APPLICATION_JSON)
// how we deserialize params
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class AnimalRestController {

    // our class is Bean(implicit Appscope), so we can inject any CDI bean into it
    @Inject
    AnimalDAO animalDAO;

    // mapstruct-generated mappers are CDI beans, we can inject them, see pom.xml#161
    @Inject
    AnimalMapper mapper;

    // using @Context we can inject contextual info from JAXRS(e.g. http request, current uri info, endpoint info...)
    @Context
    UriInfo uriInfo;

    @APIResponses({
            @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation =
                    PagedAnimalResponse.class))),
            @APIResponse(responseCode = "500")})
    @Operation(operationId = "Get Animals", description = "Returns list of animals matching given criteria, uses pagination")
    @GET
    // REST service methods should not declare exceptions, any thrown error will be transformed by exceptionMapper in tkit-rest
    // We did not define custom @Path - so it will use class level path
    public Response getAll(@BeanParam AnimalSearchCriteriaDTO dto) {
        PageResult<Animal> animals = this.animalDAO.searchByCriteria(this.mapper.map(dto));
        return Response.ok(this.mapper.map(animals)).build();
    }

    @APIResponses({
            @APIResponse(responseCode = "201", description = "OK, New animal created", content = @Content(schema = @Schema(implementation = NewAnimalDTO.class))),
            @APIResponse(responseCode = "400", description = "Client side error, invalid request"),
            @APIResponse(responseCode = "500")})
    @Operation(operationId = "createNewAnimal", description = "Stores new animal in DB")
    @POST
    @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_SAVE_OBJECT)
    // We did not define custom @Path - so it will use class level path.
    // Although we now have 2 methods with same path, it is ok, because it is a different method (get vs post)
    public Response createNewAnimal(NewAnimalDTO dto) {

        Animal created = this.animalDAO.create(this.mapper.create(dto));
        // we want to construct a link to our newly created animal
        // we take the current URI = /animals
        UriBuilder uriBuilder = this.uriInfo.getAbsolutePathBuilder();
        // and add a path element corresponding to our id/name
        uriBuilder.path(created.getId());
        return Response.created(uriBuilder.build()).build();
    }

    @APIResponses({
            @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Animal.class))),
            @APIResponse(responseCode = "404", description = "Animal not found"),
            @APIResponse(responseCode = "500")})
    @Operation(operationId = "getAnimalById", description = "Returns animal with given id")
    @GET
    @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_FIND_OBJECT)
    @Path("{id}")
    public Response getAnimalById(@Parameter(description = "Animal unique id") @PathParam("id") String id) {

        Animal animal = this.animalDAO.findById(id);
        if (animal != null) {
            return Response.ok(animal).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @APIResponses({
            @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AnimalDTO.class))),
            @APIResponse(responseCode = "404", description = "Animal not found"), @APIResponse(responseCode = "500")})
    @Operation(operationId = "deleteAnimalById", description = "Deletes the animal with given id")
    @DELETE
    @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_DELETE_OBJECT)
    @Path("{id}")
    // we add transaction here, cause we do select and then pass the detached entity to DAO for deletion
    @Transactional
    public Response deleteAnimalByName(@Parameter(description = "Animal unique id") @PathParam("id") String id) {

        Animal animal = this.animalDAO.findById(id);
        if (animal != null) {
            this.animalDAO.delete(animal);
            return Response.ok(mapper.map(animal)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    // here we simulate calling a source, that can fail
    @GET
    @Path("{id}/facts")
    // we will retry upto 4 times if this method throws given exception
    // we also delay for 300ms
    @Retry(maxRetries = 4, retryOn = IllegalStateException.class, delay = 300)
    public List<String> getAnimalFacts(@PathParam("id") String id) {

        return getFactsFromUnreliableSource(id);
    }

    private List<String> getFactsFromUnreliableSource(String id) {

        // our source will randomly fail
        boolean willFail = new Random().nextBoolean();
        if (willFail) {
            log.info("Ooops, fact fetching failed");
            throw new IllegalStateException("Unreliable source failed");
        } else {
            log.info("Cool, fact fetching succeed");
            return List.of("imagine real data here", "and also this shocking fact");
        }
    }
    
    private static class PagedAnimalResponse extends PageResultDTO<AnimalDTO> {}

}
