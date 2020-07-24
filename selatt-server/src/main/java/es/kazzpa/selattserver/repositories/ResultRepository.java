package es.kazzpa.selattserver.repositories;

import es.kazzpa.selattserver.models.AppUser;
import es.kazzpa.selattserver.models.ResultFilter;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
@Repository
public interface ResultRepository extends CrudRepository<ResultFilter, Integer> {
    List<ResultFilter> findResultFilterByPerformed_UserUploader(AppUser appUser);
}
