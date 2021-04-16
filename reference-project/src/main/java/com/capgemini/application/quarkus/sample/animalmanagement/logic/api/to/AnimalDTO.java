package com.capgemini.application.quarkus.sample.animalmanagement.logic.api.to;

import lombok.Getter;
import lombok.Setter;
import org.tkit.quarkus.rs.models.TraceableDTO;

@Getter
@Setter
public class AnimalDTO extends TraceableDTO {

    private static final long serialVersionUID = 3180533017106203282L;

    private String name;

    private String basicInfo;

    private int numberOfLegs;

}
