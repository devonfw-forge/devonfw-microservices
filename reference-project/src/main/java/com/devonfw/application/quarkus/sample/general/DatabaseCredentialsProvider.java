package com.devonfw.application.quarkus.sample.general;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.arc.Unremovable;
import io.quarkus.credentials.CredentialsProvider;

@ApplicationScoped
@Unremovable
public class DatabaseCredentialsProvider implements CredentialsProvider {

  String DB_USER_PROPERTY_VARIABLE = "QUARKUS_DATASOURCE_USERNAME";

  String DB_PASSWORD_PROPERTY_VARIABLE = "QUARKUS_DATASOURCE_USERNAME";

  @Override
  public Map<String, String> getCredentials(String credentialsProviderName) {

    Map<String, String> properties = new HashMap<>();
    properties.put(USER_PROPERTY_NAME, System.getenv(this.DB_USER_PROPERTY_VARIABLE));
    properties.put(PASSWORD_PROPERTY_NAME, System.getenv(this.DB_PASSWORD_PROPERTY_VARIABLE));
    return properties;
  }

}