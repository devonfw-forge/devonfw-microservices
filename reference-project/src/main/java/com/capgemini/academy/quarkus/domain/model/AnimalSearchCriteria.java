package com.capgemini.academy.quarkus.domain.model;

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
