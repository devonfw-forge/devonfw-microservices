package com.devonfw.demoquarkus.domain.dao;

import static org.tkit.quarkus.jpa.utils.QueryCriteriaUtil.wildcard;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.devonfw.demoquarkus.domain.model.AnimalSearchCriteria;
import com.devonfw.demoquarkus.domain.model.Animal;
import com.devonfw.demoquarkus.domain.model.Animal_;
import io.micrometer.core.annotation.Timed;
import org.eclipse.microprofile.opentracing.Traced;
import org.tkit.quarkus.jpa.daos.AbstractDAO;
import org.tkit.quarkus.jpa.daos.Page;
import org.tkit.quarkus.jpa.daos.PageResult;

import io.micrometer.core.annotation.Counted;
import io.opentracing.Tracer;
import lombok.extern.slf4j.Slf4j;

/**
 * Our Animal DAO(Data access object).
 * We mark it as Applicationscoped - single instance for app
 */
@ApplicationScoped
@Slf4j
// add all operations on this bean to tracer
@Traced
// extending AbstractDAO gives us CRUD for free
@Timed
public class AnimalDAO extends AbstractDAO<Animal> {

  @Inject
  EntityManager em;

  // you can inject current tracer instance like any other bean
  @Inject
  Tracer tracer;

  @Counted(value = "metric.searchByCriteria", description = "Number of times Dao.searchByCriteria was called", extraTags = {
  "layer", "domain" })
  public PageResult<Animal> searchByCriteria(AnimalSearchCriteria criteria) {

    if (criteria == null) {
      return null;
    }
    CriteriaQuery<Animal> cq = criteriaQuery();
    Root<Animal> root = cq.from(Animal.class);
    List<Predicate> predicates = new ArrayList<>();
    // here we use typesafe JPA criteria API
    CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();

    if (criteria.getName() != null && !criteria.getName().isEmpty()) {
      predicates.add(cb.like(root.get(Animal_.NAME), wildcard(criteria.getName())));
    }
    if (criteria.getNumberOfLegs() != null) {
      predicates.add(cb.equal(root.get(Animal_.NUMBER_OF_LEGS), criteria.getNumberOfLegs()));
    }
    if (!predicates.isEmpty()) {
      cq.where(predicates.toArray(new Predicate[0]));
    }
    // we can add useful info to our span
    this.tracer.activeSpan().setTag("predicates", predicates.size()).setTag("layer", "domain");
    // tkit jpa allows to simply convert existing query to paged query
    return createPageQuery(cq, Page.of(criteria.getPageNumber(), criteria.getPageSize())).getPageResult();
  }

}
