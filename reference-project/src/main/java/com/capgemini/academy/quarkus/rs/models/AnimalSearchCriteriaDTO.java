package com.capgemini.academy.quarkus.rs.models;


import lombok.Getter;
import lombok.Setter;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

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

