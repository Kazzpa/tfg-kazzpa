package es.kazzpa.selattserver.services;

import es.kazzpa.selattserver.models.ClassifierResult;
import org.springframework.http.ResponseEntity;
import weka.core.Instances;

public interface EvaluationService {
    ResponseEntity<ClassifierResult> handleNaiveBayes(String fileName) throws Exception;
    ResponseEntity<ClassifierResult> applyNaiveBayes (String filename, Instances trainingData) throws Exception;
}
