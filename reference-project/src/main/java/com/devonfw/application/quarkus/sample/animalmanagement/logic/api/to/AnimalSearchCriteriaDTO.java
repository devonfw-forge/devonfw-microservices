package com.devonfw.application.quarkus.sample.animalmanagement.logic.api.to;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimalSearchCriteriaDTO {

  @QueryParam("name")
  private String name;

  @QueryParam("legs")
  private Integer numberOfLegs;

  @QueryParam("page")
  @DefaultValue("0")
  private int pageNumber = 0;

  @QueryParam("size")
  @DefaultValue("100")
  private int pageSize = 100;
}
