package com.devonfw.demoquarkus.rest.v1.controller;

import com.devonfw.demoquarkus.rest.v1.mapper.AnimalMapper;
import com.devonfw.demoquarkus.domain.model.Animal;
import com.devonfw.demoquarkus.domain.repo.AnimalRepository;
import com.devonfw.demoquarkus.rest.v1.model.AnimalDTO;
import com.devonfw.demoquarkus.rest.v1.model.AnimalSearchCriteriaDTO;
import com.devonfw.demoquarkus.rest.v1.model.NewAnimalDTO;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.springframework.data.domain.Page;
import org.tkit.quarkus.rs.models.PageResultDTO;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import java.util.List;
import java.util.Random;

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
    AnimalRepository animalRepository;

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
    	Iterable<Animal> animals = this.animalRepository.findAll();
     	return Response.ok(animals).build();
    }
    
    @GET
    @Path("criteriaApi")
    public Response getAllCriteriaApi(@BeanParam AnimalSearchCriteriaDTO dto) {
    	Page<Animal> page = this.animalRepository.findAllCriteriaApi(dto);
     	return Response.ok(page).build();
    }
    
    @GET
    @Path("queryDsl")
    public Response getAllQueryDsl(@BeanParam AnimalSearchCriteriaDTO dto) {
    	Page<Animal> page = this.animalRepository.findAllQueryDsl(dto);
     	return Response.ok(page).build();
    }
    
    @GET
    @Path("query")
    public Response getAllQuery(@BeanParam AnimalSearchCriteriaDTO dto) {
    	List<Animal> animals = this.animalRepository.findByNameQuery(dto);
     	return Response.ok(animals).build();
    }
    
    @GET
    @Path("nativeQuery")
    public Response getAllNativeQuery(@BeanParam AnimalSearchCriteriaDTO dto) {
    	List<Animal> animals = this.animalRepository.findByNameNativeQuery(dto);
     	return Response.ok(animals).build();
    }
    
    @GET
    @Path("ordered")
    public Response getAllOrderedByName() {
    	Page<Animal> page = this.animalRepository.findAllByOrderByName();
     	return Response.ok(page).build();
    }

    @APIResponses({
            @APIResponse(responseCode = "201", description = "OK, New animal created", content = @Content(schema = @Schema(implementation = NewAnimalDTO.class))),
            @APIResponse(responseCode = "400", description = "Client side error, invalid request"),
            @APIResponse(responseCode = "500")})
    @Operation(operationId = "createNewAnimal", description = "Stores new animal in DB")
    @POST
    // We did not define custom @Path - so it will use class level path.
    // Although we now have 2 methods with same path, it is ok, because it is a different method (get vs post)
    public Response createNewAnimal(NewAnimalDTO dto) {

    	Animal created = this.animalRepository.save(this.mapper.create(dto));
        // we want to construct a link to our newly created animal
        // we take the current URI = /animals
        UriBuilder uriBuilder = this.uriInfo.getAbsolutePathBuilder();
        // and add a path element corresponding to our id/name
        uriBuilder.path(String.valueOf(created.getId()));
        return Response.created(uriBuilder.build()).build();
    }

    @APIResponses({
            @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Animal.class))),
            @APIResponse(responseCode = "404", description = "Animal not found"),
            @APIResponse(responseCode = "500")})
    @Operation(operationId = "getAnimalById", description = "Returns animal with given id")
    @GET
    @Path("{id}")
    public Response getAnimalById(@Parameter(description = "Animal unique id") @PathParam("id") String id) {

    	Animal animal = this.animalRepository.findById(Long.valueOf(id)).get();
        if (animal != null) {
            return Response.ok(animal).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    @GET
    @Path("name/{name}")
    public Response getAnimalByName(@PathParam("name") String name) {

    	Animal animal = this.animalRepository.findByName(name);
        if (animal != null) {
            return Response.ok(animal).build();
        } else {
            return Response.noContent().build();
        }
    }

    @APIResponses({
            @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AnimalDTO.class))),
            @APIResponse(responseCode = "404", description = "Animal not found"), @APIResponse(responseCode = "500")})
    @Operation(operationId = "deleteAnimalById", description = "Deletes the animal with given id")
    @DELETE
    @Path("{id}")
    // we add transaction here, cause we do select and then pass the detached entity to DAO for deletion
    @Transactional
    public Response deleteAnimalByName(@Parameter(description = "Animal unique id") @PathParam("id") String id) {

    	Animal animal = this.animalRepository.findById(Long.valueOf(id)).get();
        if (animal != null) {
            this.animalRepository.delete(animal);
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
