package es.kazzpa.selattserver.services;

import es.kazzpa.selattserver.models.ResultFilter;
import org.springframework.http.ResponseEntity;
import weka.core.Instances;

public interface EvaluationService {
    ResponseEntity<ResultFilter> handleNaiveBayes(String fileName) throws Exception;
    ResponseEntity<ResultFilter> applyNaiveBayes (String filename,Instances trainingData) throws Exception;
}
