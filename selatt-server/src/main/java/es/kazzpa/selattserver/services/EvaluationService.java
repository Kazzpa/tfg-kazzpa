package es.kazzpa.selattserver.services;

import weka.core.Instances;

public interface EvaluationService {
    String handleNaiveBayes(String fileName) throws Exception;
    String applyNaiveBayes (String filename,Instances trainingData) throws Exception;
}
