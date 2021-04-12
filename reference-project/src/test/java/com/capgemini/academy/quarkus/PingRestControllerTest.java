package com.capgemini.academy.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class PingRestControllerTest {

    @Test
    public void testPing() {
        //we are testing the app by actually calling its HTTP API, just like a real client would
        //no mocks here
        String responseText = given().accept(ContentType.TEXT)
                .when()
                .log().all()
                .get("/ping")
                .then()
                .statusCode(200)
                .extract().asString();

        assertEquals(responseText, "Hello from Quarkus");

    }

    @Test
    public void testPingAsResponse() {
        given()
                .when()
                .log().all()
                .get("/ping/pingResponse")
                .then()
                .statusCode(200)
                .body("state", equalTo("ok"))
                .header("X-Hi", "Hi from quarkus");


    }

}