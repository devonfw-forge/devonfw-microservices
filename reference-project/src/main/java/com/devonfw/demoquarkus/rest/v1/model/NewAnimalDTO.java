package com.devonfw.demoquarkus.rest.v1.model;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewAnimalDTO {

  @Schema(nullable = false, description = "Animal name", minLength = 3, maxLength = 50)
  private String name;

  @Schema(description = "Animal tag line", minLength = 3, maxLength = 50)
  private String basicInfo;

  @Schema(type = SchemaType.INTEGER, description = "How many legs does the animal have", example = "3")
  private int numberOfLegs;

}
