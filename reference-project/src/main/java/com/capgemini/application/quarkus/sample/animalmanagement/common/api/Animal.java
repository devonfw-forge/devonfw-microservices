package com.capgemini.application.quarkus.sample.animalmanagement.common.api;

import lombok.Getter;
import lombok.Setter;
import org.tkit.quarkus.jpa.models.TraceableEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity
//A JPA entity requires at least 2 things @Entity annotation and an ID
//by default, the DB table will have the same name as our class
//tracebleEntity has GUID as ID
public class Animal  extends TraceableEntity {
    //name was never a good ID
    private String name;
    //every primitive attribute on this class will be represented as column in animal table
    private String basicInfo;

    private int numberOfLegs;

}
