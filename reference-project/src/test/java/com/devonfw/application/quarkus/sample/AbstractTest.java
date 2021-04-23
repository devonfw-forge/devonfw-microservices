package com.devonfw.application.quarkus.sample;

import io.quarkus.test.common.QuarkusTestResource;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.tkit.quarkus.test.docker.DockerComposeService;
import org.tkit.quarkus.test.docker.DockerComposeTestResource;
import org.tkit.quarkus.test.docker.DockerService;
import org.tkit.quarkus.test.docker.QuarkusTestcontainers;

/**
 * Simple base class, that gives us support for test containers + tkit-test
 */
@QuarkusTestcontainers
@QuarkusTestResource(DockerComposeTestResource.class)
public abstract class AbstractTest {

    @DockerService("demo")
    protected DockerComposeService service;

    @BeforeEach
    public void before() {
        if (service != null) {
            RestAssured.port = service.getPort(8080);
            RestAssured.baseURI = "http://" + service.getHost();
        }
    }
}