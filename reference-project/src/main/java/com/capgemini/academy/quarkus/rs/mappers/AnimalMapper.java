package com.capgemini.academy.quarkus.rs.mappers;

import com.capgemini.academy.quarkus.domain.model.Animal;
import com.capgemini.academy.quarkus.domain.model.AnimalSearchCriteria;
import com.capgemini.academy.quarkus.rs.models.AnimalDTO;
import com.capgemini.academy.quarkus.rs.models.AnimalSearchCriteriaDTO;
import com.capgemini.academy.quarkus.rs.models.NewAnimalDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.tkit.quarkus.jpa.daos.PageResult;
import org.tkit.quarkus.rs.mappers.OffsetDateTimeMapper;
import org.tkit.quarkus.rs.models.PageResultDTO;

import java.util.stream.Stream;

//mapstruct will generate an impl class from this interface at compile time
@Mapper(uses = OffsetDateTimeMapper.class)
public interface AnimalMapper {

    AnimalDTO map(Animal model);

    Stream<AnimalDTO> map(Stream<Animal> model);

    Animal create(NewAnimalDTO dto);

    AnimalSearchCriteria map(AnimalSearchCriteriaDTO dto);

    PageResultDTO<AnimalDTO> map(PageResult<Animal> page);
}
