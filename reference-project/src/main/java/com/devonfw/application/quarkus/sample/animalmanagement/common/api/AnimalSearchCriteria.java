package com.devonfw.application.quarkus.sample.animalmanagement.common.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimalSearchCriteria {

  private String name;

  private Integer numberOfLegs;

  private Integer pageNumber;

  private Integer pageSize;
}
