package es.kazzpa.selattserver.services;

import es.kazzpa.selattserver.models.ClassifierResult;
import es.kazzpa.selattserver.models.FeatureResult;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import weka.core.Instances;

import java.util.List;

public interface EvaluationService {
    ResponseEntity<ClassifierResult> handleNaiveBayes(String fileName) throws Exception;
    ResponseEntity<ClassifierResult> handleNaiveBayes(FeatureResult dataset) throws Exception;
    ResponseEntity<ClassifierResult> applyNaiveBayes (ClassifierResult rf,String filename, Instances trainingData) throws Exception;
    List<ClassifierResult> getResultsByUser(Authentication authentication) throws Exception;
    List<ClassifierResult> getNewResultsByUser(Authentication authentication) throws Exception;
    List<FeatureResult> datasetsByUser(Authentication authentication) throws Exception;
    void setResultSeen(ClassifierResult classifierResult) throws Exception;

}
