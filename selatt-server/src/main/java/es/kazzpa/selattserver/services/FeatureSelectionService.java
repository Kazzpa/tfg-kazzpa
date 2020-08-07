package es.kazzpa.selattserver.services;

import es.kazzpa.selattserver.models.Algorithm;
import es.kazzpa.selattserver.models.Dataset;
import es.kazzpa.selattserver.models.ResultFilter;
import es.kazzpa.selattserver.repositories.ResultRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import upo.jml.data.dataset.ClassificationDataset;
import weka.core.Instances;

import java.util.List;

public interface FeatureSelectionService {
    void handleLoadDefaultData();

    ResultFilter handlePCAFeatures() throws Exception;

    String handleRandomizedProjectionFeatures() throws Exception;

    String handleCFSSubsetEval() throws Exception;

    ResponseEntity<ResultFilter> handleFCBF(String datasetName) throws Exception;

    ResponseEntity<ResultFilter> handleScatterSearch(String datasetName) throws Exception;

    ResponseEntity<ResultFilter> handleVNS(String datasetName) throws Exception;

    ResultFilter ApplyPCA(String name, Instances trainingData) throws Exception;

    Instances applyRP(Instances trainingData, int numAttributes) throws Exception;

    ResultFilter applyPCAFilter(Instances trainingData, int numAttributes) throws Exception;

    String applyCfsSubsetEval(Instances data) throws Exception;

    ResponseEntity<ResultFilter> applyFCBF(String name, Instances trainingData) throws Exception;

    ResponseEntity<ResultFilter> applyScatterSearch(String fileName, Instances trainingData) throws Exception;

    ResponseEntity<ResultFilter> applyVNS(String filename, ClassificationDataset dataset) throws Exception;

    ResultFilter plotPCA() throws Exception;

    void plotRP() throws Exception;

    public List<ResultFilter> resultsByUser(Authentication authentication) throws Exception;

    List<Dataset> datasetsByUser(Authentication authentication) throws Exception;


}
