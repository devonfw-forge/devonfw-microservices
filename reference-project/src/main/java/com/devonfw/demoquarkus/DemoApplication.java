package com.devonfw.demoquarkus;

import javax.ws.rs.core.Application;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(tags = {
@Tag(name = "animal", description = "Animal API.") }, info = @Info(title = "My Demo App RESTful API", version = "1.0.0", contact = @Contact(name = "API Support", email = "support@acme.com")))
/**
 * JaxRS application is not required in quarkus, but it is useful to place central API docs somewhere.
 * We could also use package-info.java for this.
 */
public class DemoApplication extends Application {
}