package es.kazzpa.selattserver.repositories;

import es.kazzpa.selattserver.models.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
@Repository
public interface ClassifierResultRepository extends CrudRepository<ClassifierResult, Integer> {
    List<ClassifierResult> findClassifierResultByPerformed_UserUploader(AppUser appUser);
    List<ClassifierResult> findClassifierResultByPerformed_UserUploaderAndSeenFalse(AppUser appUser);
    ClassifierResult findClassifierResultByPerformedAndAlgorithm(Dataset performed, Algorithm algorithm);
    ClassifierResult findClassifierResultByPerformedAndAlgorithmAndFeatureAlgorithm(Dataset performed,Algorithm algorithm, Algorithm featureAlgorithm);
}
