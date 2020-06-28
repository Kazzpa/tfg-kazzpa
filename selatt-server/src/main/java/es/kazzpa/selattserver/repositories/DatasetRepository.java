package es.kazzpa.selattserver.repositories;

import es.kazzpa.selattserver.models.Dataset;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatasetRepository extends CrudRepository<Dataset, String> {
    Dataset findDatasetByName(String name);
}
