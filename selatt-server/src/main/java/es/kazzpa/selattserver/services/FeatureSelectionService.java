package es.kazzpa.selattserver.services;

import es.kazzpa.selattserver.models.Dataset;
import es.kazzpa.selattserver.models.ResultFilter;
import es.kazzpa.selattserver.repositories.ResultRepository;
import org.springframework.security.core.Authentication;
import upo.jml.data.dataset.ClassificationDataset;
import weka.core.Instances;

import java.util.List;

public interface FeatureSelectionService {
    void handleLoadDefaultData();

    ResultFilter handlePCAFeatures() throws Exception;

    String handleRandomizedProjectionFeatures() throws Exception;

    String handleCFSSubsetEval() throws Exception;

    ResultFilter handleFCBF(String datasetName) throws Exception;

    ResultFilter handleScatterSearch(String datasetName) throws Exception;

    ResultFilter handleVNS(String datasetName) throws Exception;

    ResultFilter ApplyPCA(String name, Instances trainingData) throws Exception;

    Instances applyRP(Instances trainingData, int numAttributes) throws Exception;

    ResultFilter applyPCAFilter(Instances trainingData, int numAttributes) throws Exception;

    String applyCfsSubsetEval(Instances data) throws Exception;

    ResultFilter applyFCBF(String name, Instances trainingData) throws Exception;

    ResultFilter applyScatterSearch(String fileName, Instances trainingData) throws Exception;

    ResultFilter applyVNS(String filename, ClassificationDataset dataset) throws Exception;

    ResultFilter plotPCA() throws Exception;

    void plotRP() throws Exception;

    public List<ResultFilter> resultsByUser(Authentication authentication) throws Exception;

    List<Dataset> datasetsByUser(Authentication authentication) throws Exception;

    ResultFilter composeResultFilter(int[] solution, String algorithm, String datasetName) throws Exception;
}
