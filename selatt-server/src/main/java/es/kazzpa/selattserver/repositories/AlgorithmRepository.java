package es.kazzpa.selattserver.repositories;

import es.kazzpa.selattserver.models.Algorithm;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlgorithmRepository extends CrudRepository<Algorithm, String> {
    Algorithm findAlgorithmByName(String name);
}
