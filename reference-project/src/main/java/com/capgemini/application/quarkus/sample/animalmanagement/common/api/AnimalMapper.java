package com.capgemini.application.quarkus.sample.animalmanagement.common.api;

import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.tkit.quarkus.jpa.daos.PageResult;
import org.tkit.quarkus.rs.mappers.OffsetDateTimeMapper;
import org.tkit.quarkus.rs.models.PageResultDTO;

import com.capgemini.application.quarkus.sample.animalmanagement.common.api.Animal;
import com.capgemini.application.quarkus.sample.animalmanagement.common.api.AnimalSearchCriteria;
import com.capgemini.application.quarkus.sample.animalmanagement.logic.api.to.AnimalDTO;
import com.capgemini.application.quarkus.sample.animalmanagement.logic.api.to.AnimalSearchCriteriaDTO;
import com.capgemini.application.quarkus.sample.animalmanagement.logic.api.to.NewAnimalDTO;

//mapstruct will generate an impl class from this interface at compile time
@Mapper(uses = OffsetDateTimeMapper.class)
public interface AnimalMapper {

  AnimalDTO map(Animal model);

  Stream<AnimalDTO> map(Stream<Animal> model);

  Animal create(NewAnimalDTO dto);

  AnimalSearchCriteria map(AnimalSearchCriteriaDTO dto);

  PageResultDTO<AnimalDTO> map(PageResult<Animal> page);
}
