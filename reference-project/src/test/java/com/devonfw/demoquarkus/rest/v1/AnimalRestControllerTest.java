package com.devonfw.demoquarkus.rest.v1;

import com.devonfw.demoquarkus.rest.v1.model.AnimalDTO;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.tkit.quarkus.rs.models.PageResultDTO;
import org.tkit.quarkus.test.WithDBData;
import org.tkit.quarkus.test.docker.DockerComposeTestResource;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//Before you run this test, tkit-test extension starts docker containers from resources/docker-compose.yaml
//we get a real postgresdb for our tests which will be stopped after tests. No manual test setup is needed.
@QuarkusTest
@QuarkusTestResource(DockerComposeTestResource.class)
class AnimalRestControllerTest {//extends AbstractTest {

    @Test
    // we also started a micro container, that can populated DB with data from excel
    // annotating class or method with @WithDBData allows us to scope data for each test even if we use the same DB
    @WithDBData(value = "data/animal.xls", deleteBeforeInsert = true)
    void getAll() {

        Response response = given().when().contentType(MediaType.APPLICATION_JSON).get("/animals").then().statusCode(200)
                // .body("$.size()", equalTo(2))
                .extract().response();

        PageResultDTO<AnimalDTO> animalsReturned = response.as(new TypeRef<PageResultDTO<AnimalDTO>>() {
        });

        // we import data from /import.sql - ergo expect 1 result
        assertEquals(2, animalsReturned.getTotalElements());
    }

    @Test
    void getNonExistingTest() {

        Response response = given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .get("/animals/doesnoexist")
                .then()
                .log()
                .all()
                .statusCode(404).extract().response();
    }

    @Test
    @WithDBData(value = "data/empty.xls", deleteBeforeInsert = true)
    void createNewAnimal() {

        AnimalDTO animal = new AnimalDTO();
        animal.setName("Dog");
        animal.setBasicInfo("Live");
        animal.setNumberOfLegs(4);

        Response response = given()
                .when()
                .body(animal)
                .contentType(MediaType.APPLICATION_JSON)
                .post("/animals")
                .then()
                .log().all()
                .statusCode(201)
                .header("Location", notNullValue())
                .extract().response();

        assertEquals(201, response.statusCode());

        response = given()
                .when()
                .contentType(MediaType.APPLICATION_JSON)
                .get("/animals")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response();

        PageResultDTO<AnimalDTO> animalsReturned = response.as(new TypeRef<>() {
        });
        assertEquals(1, animalsReturned.getTotalElements());
        AnimalDTO created = animalsReturned.getStream().get(0);
        assertNotNull(created);
        assertEquals(animal.getName(), created.getName());
    }

    @Test
    @WithDBData(value = "data/animal.xls", deleteBeforeInsert = true)
    public void testGetById() {

        given()
                .when()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON)
                .get("/animals/1")
                .then()
                .statusCode(200)
                .body("basicInfo", equalTo("Cat"));
    }

    @Test
    @WithDBData(value = "data/animal.xls", deleteBeforeInsert = true)
    public void deleteById() {

        // delete
        given().when().log().all().contentType(MediaType.APPLICATION_JSON).delete("/animals/1").then().statusCode(200)
                .body("name", equalTo("Kitty"));

        // after deletion it should be deleted
        given().when().log().all().contentType(MediaType.APPLICATION_JSON).get("/animals/1").then().statusCode(404);

    }

}