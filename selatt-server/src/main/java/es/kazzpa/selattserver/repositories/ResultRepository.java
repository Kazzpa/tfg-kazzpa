package es.kazzpa.selattserver.repositories;

import es.kazzpa.selattserver.models.ResultFilter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
@Repository
public interface ResultRepository extends CrudRepository<ResultFilter, Integer> {
}
