package es.kazzpa.selattserver.repositories;

import es.kazzpa.selattserver.models.Algorithm;
import es.kazzpa.selattserver.models.AppUser;
import es.kazzpa.selattserver.models.Dataset;
import es.kazzpa.selattserver.models.ClassifierResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
@Repository
public interface ClassifierResultRepository extends CrudRepository<ClassifierResult, Integer> {
    List<ClassifierResult> findClassifierResultByPerformed_UserUploader(AppUser appUser);
    ClassifierResult findClassifierResultByPerformedAndAlgorithm(Dataset performed, Algorithm algorithm);
}
