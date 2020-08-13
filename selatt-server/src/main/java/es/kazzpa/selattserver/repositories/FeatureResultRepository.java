package es.kazzpa.selattserver.repositories;

import es.kazzpa.selattserver.models.Algorithm;
import es.kazzpa.selattserver.models.AppUser;
import es.kazzpa.selattserver.models.Dataset;
import es.kazzpa.selattserver.models.FeatureResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeatureResultRepository extends CrudRepository<FeatureResult,String> {

    FeatureResult findByAlgorithmAndPerformed(Algorithm algorithm, Dataset original);
    List<FeatureResult> findFeatureResultByPerformed_UserUploader(AppUser appUser);
}
