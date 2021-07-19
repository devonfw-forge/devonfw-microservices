package com.devonfw.demoquarkus.domain.repo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import com.devonfw.demoquarkus.domain.model.Animal;
import com.devonfw.demoquarkus.domain.model.Animal_;
import com.devonfw.demoquarkus.domain.model.QAnimal;
import com.devonfw.demoquarkus.rest.v1.model.AnimalSearchCriteriaDTO;
import com.querydsl.jpa.impl.JPAQuery;

public class AnimalFragmentImpl implements AnimalFragment {
	
	@Inject
	EntityManager em;

	@Override
	public Page<Animal> findAllCriteriaApi(AnimalSearchCriteriaDTO dto) {
		CriteriaQuery<Animal> cq = this.em.getCriteriaBuilder().createQuery(Animal.class);
		Root<Animal> root = cq.from(Animal.class);
		List<Predicate> predicates = new ArrayList<>();
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		if (dto.getName() != null && !dto.getName().isEmpty()) {
			predicates.add(cb.like(root.get(Animal_.NAME), dto.getName()));
		}
		if (dto.getNumberOfLegs() != null) {
			predicates.add(cb.equal(root.get(Animal_.NUMBER_OF_LEGS), dto.getNumberOfLegs()));
		}
		if (!predicates.isEmpty()) {
			cq.where(predicates.toArray(new Predicate[0]));
		}
		
		//Order by name
	    cq.orderBy(cb.desc(root.get(Animal_.NAME)));	
		
	    TypedQuery<Animal> animals = this.em.createQuery(cq).setFirstResult(dto.getPageNumber() * dto.getPageSize()).setMaxResults(dto.getPageSize());
		return new PageImpl<Animal>(animals.getResultList(), PageRequest.of(dto.getPageNumber(), dto.getPageSize()), animals.getResultList().size());
	}
	
	@Override
	public Page<Animal> findAllQueryDsl(AnimalSearchCriteriaDTO dto) {
		QAnimal animal = QAnimal.animal;
		JPAQuery<Animal> query = new JPAQuery<Animal>(this.em);
		query.from(animal);
		if (dto.getName() != null && !dto.getName().isEmpty()) {
			query.where(animal.name.eq(dto.getName()));
		}
		if (dto.getNumberOfLegs() != null) {
			query.where(animal.numberOfLegs.eq(dto.getNumberOfLegs()));
		}
		
		//Order by name
		query.orderBy(animal.name.desc());
		
		List<Animal> animals = query.limit(dto.getPageSize()).offset(dto .getPageNumber() * dto.getPageSize()).fetch();
		return new PageImpl<>(animals, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), animals.size());
	}

	@Override
	public List<Animal> findByNameQuery(AnimalSearchCriteriaDTO dto) {
		Query query = this.em.createQuery("select a from Animal a where a.name = :name");
		query.setParameter("name", dto.getName());
		return query.getResultList();
	}
	
	@Override
	public List<Animal> findByNameNativeQuery(AnimalSearchCriteriaDTO dto) {
		Query query = this.em.createNativeQuery("select * from Animal where name = :name");
		query.setParameter("name", dto.getName());
		return query.getResultList();
	}
}
