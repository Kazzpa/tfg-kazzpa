package es.kazzpa.selattserver.repositories;

import es.kazzpa.selattserver.models.Attribute;
import es.kazzpa.selattserver.models.ResultFilter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeRepository extends CrudRepository<Attribute, Integer> {
}

