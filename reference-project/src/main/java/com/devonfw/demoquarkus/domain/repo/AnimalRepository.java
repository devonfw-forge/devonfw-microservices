package com.devonfw.demoquarkus.domain.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.devonfw.demoquarkus.domain.model.Animal;

public interface AnimalRepository extends CrudRepository<Animal, Long>, AnimalFragment {
	
	@Query("select a from Animal a where name = :name")
	Animal findByName(@Param("name") String name);
	
	Page<Animal> findAllByOrderByName();
}
