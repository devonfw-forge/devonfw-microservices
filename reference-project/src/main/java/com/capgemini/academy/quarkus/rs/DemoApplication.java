package com.capgemini.academy.quarkus.rs;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(
    tags = {
            @Tag(name="animal", description="Animal operations."),
            @Tag(name="ping", description="Ping operations")
    },
    info = @Info(
        title="My Demo App RESTful API",
        version = "1.0.0",
        contact = @Contact(
            name = "API Support",
            email = "support@acme.com"),
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0.html"))
)
/**
 * JaxRS application is not required in quarkus, but it is useful to place central API docs somewhere.
 */
public class DemoApplication extends Application {
}