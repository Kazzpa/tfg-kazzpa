package es.kazzpa.selattserver.services;

import es.kazzpa.selattserver.models.Dataset;
import es.kazzpa.selattserver.models.FeatureResult;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import upo.jml.data.dataset.ClassificationDataset;
import weka.core.Instances;

import java.util.List;

public interface FeatureSelectionService {
    void handleLoadDefaultData();

    FeatureResult handlePCAFeatures() throws Exception;

    String handleRandomizedProjectionFeatures() throws Exception;

    String handleCFSSubsetEval() throws Exception;

    ResponseEntity<FeatureResult> handleFCBF(String datasetName) throws Exception;

    ResponseEntity<FeatureResult> handleScatterSearch(String datasetName) throws Exception;

    ResponseEntity<FeatureResult> handleVNS(String datasetName) throws Exception;

    FeatureResult ApplyPCA(String name, Instances trainingData) throws Exception;

    Instances applyRP(Instances trainingData, int numAttributes) throws Exception;

    FeatureResult applyPCAFilter(Instances trainingData, int numAttributes) throws Exception;

    String applyCfsSubsetEval(Instances data) throws Exception;

    ResponseEntity<FeatureResult> applyFCBF(String name, Instances trainingData) throws Exception;

    ResponseEntity<FeatureResult> applyScatterSearch(String fileName, Instances trainingData) throws Exception;

    ResponseEntity<FeatureResult> applyVNS(String filename, ClassificationDataset dataset) throws Exception;

    FeatureResult plotPCA() throws Exception;

    void plotRP() throws Exception;

    public List<FeatureResult> resultsByUser(Authentication authentication) throws Exception;

    List<Dataset> datasetsByUser(Authentication authentication) throws Exception;


}
