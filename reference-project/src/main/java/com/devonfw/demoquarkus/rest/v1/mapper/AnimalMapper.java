package com.devonfw.demoquarkus.rest.v1.mapper;

import java.util.stream.Stream;

import com.devonfw.demoquarkus.domain.model.AnimalSearchCriteria;
import com.devonfw.demoquarkus.domain.model.Animal;
import com.devonfw.demoquarkus.rest.v1.model.AnimalSearchCriteriaDTO;
import org.mapstruct.Mapper;
import org.tkit.quarkus.rs.mappers.OffsetDateTimeMapper;

import com.devonfw.demoquarkus.rest.v1.model.AnimalDTO;
import com.devonfw.demoquarkus.rest.v1.model.NewAnimalDTO;

//mapstruct will generate an impl class(CDI bean, see pom.xml) from this interface at compile time
@Mapper(uses = OffsetDateTimeMapper.class)
public interface AnimalMapper {

  AnimalDTO map(Animal model);

  Stream<AnimalDTO> map(Stream<Animal> model);

  Animal create(NewAnimalDTO dto);

  AnimalSearchCriteria map(AnimalSearchCriteriaDTO dto);
}
