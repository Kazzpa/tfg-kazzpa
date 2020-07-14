package es.kazzpa.selattserver.services;

import es.kazzpa.selattserver.models.ResultFilter;
import es.kazzpa.selattserver.repositories.ResultRepository;
import upo.jml.data.dataset.ClassificationDataset;
import weka.core.Instances;

public interface FeatureSelectionService {
    void handleLoadDefaultData();

    ResultFilter handlePCAFeatures() throws Exception;

    String handleRandomizedProjectionFeatures() throws Exception;

    String handleCFSSubsetEval() throws Exception;

    String handleFCBF(String datasetName) throws Exception;

    String handleScatterSearch(String datasetName) throws Exception;

    String handleVNS(String datasetName) throws Exception;

    ResultFilter ApplyPCA(String name, Instances trainingData) throws Exception;

    Instances applyRP(Instances trainingData, int numAttributes) throws Exception;

    ResultFilter applyPCAFilter(Instances trainingData, int numAttributes) throws Exception;

    String applyCfsSubsetEval(Instances data) throws Exception;

    String applyFCBF(String name, Instances trainingData) throws Exception;

    String applyScatterSearch(String fileName, Instances trainingData) throws Exception;

    String applyVNS(String filename, ClassificationDataset dataset) throws Exception;

    ResultFilter plotPCA() throws Exception;

    void plotRP() throws Exception;

}
