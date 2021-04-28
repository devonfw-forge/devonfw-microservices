package com.devonfw.demoquarkus.domain.model;

import javax.persistence.Entity;

import org.tkit.quarkus.jpa.models.TraceableEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
// A JPA entity requires at least 2 things @Entity annotation and an ID
// by default, the DB table will have the same name as our class
// tracebleEntity has GUID as ID
public class Animal extends TraceableEntity {
  // name was never a good ID
  private String name;

  // every primitive attribute on this class will be represented as column in animal table
  private String basicInfo;

  private int numberOfLegs;

}
