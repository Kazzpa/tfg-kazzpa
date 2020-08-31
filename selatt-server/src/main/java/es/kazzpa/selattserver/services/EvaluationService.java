package es.kazzpa.selattserver.services;

import es.kazzpa.selattserver.models.ClassifierResult;
import es.kazzpa.selattserver.models.FeatureResult;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import weka.core.Instances;

import java.util.List;

public interface EvaluationService {
    ResponseEntity<ClassifierResult> handleNaiveBayes(String fileName) throws Exception;
    ResponseEntity<ClassifierResult> handleBayesNet(String fileName) throws Exception;
    ResponseEntity<ClassifierResult> handleIbk(String fileName) throws Exception;
    ResponseEntity<ClassifierResult> handleMlp(String fileName) throws Exception;
    ResponseEntity<ClassifierResult> handleHiddenNaiveBayes(String fileName) throws Exception;

    ResponseEntity<ClassifierResult> handleNaiveBayes(FeatureResult dataset) throws Exception;
    ResponseEntity<ClassifierResult> handleBayesNet(FeatureResult dataset) throws Exception;
    ResponseEntity<ClassifierResult> handleHiddenNaiveBayes(FeatureResult dataset) throws Exception;
    ResponseEntity<ClassifierResult> handleIbk(FeatureResult dataset) throws Exception;
    ResponseEntity<ClassifierResult> handleMlp(FeatureResult dataset) throws Exception;
    ResponseEntity<ClassifierResult> applyNaiveBayes (ClassifierResult rf,String filename, Instances trainingData) throws Exception;
    ResponseEntity<ClassifierResult> applyBayesNet (ClassifierResult rf,String filename, Instances trainingData) throws Exception;
    List<ClassifierResult> getResultsByUser(Authentication authentication) throws Exception;
    List<ClassifierResult> getNewResultsByUser(Authentication authentication) throws Exception;
    List<FeatureResult> datasetsByUser(Authentication authentication) throws Exception;
    void setResultSeen(ClassifierResult classifierResult) throws Exception;

}
