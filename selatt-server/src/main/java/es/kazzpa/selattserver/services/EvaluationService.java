package es.kazzpa.selattserver.services;

import weka.core.Instances;

public interface EvaluationService {
    public String naiveBayesProcess (String filename) throws Exception;
}
