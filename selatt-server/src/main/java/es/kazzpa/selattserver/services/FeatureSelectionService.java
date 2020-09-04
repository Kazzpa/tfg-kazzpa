package es.kazzpa.selattserver.services;

import es.kazzpa.selattserver.models.ClassifierResult;
import es.kazzpa.selattserver.models.Dataset;
import es.kazzpa.selattserver.models.FeatureResult;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import upo.jml.data.dataset.ClassificationDataset;
import weka.core.Instances;

import java.util.List;

public interface FeatureSelectionService {
    void handleLoadDefaultData();

    ResponseEntity<FeatureResult> handleFCBF(String datasetName) throws Exception;

    ResponseEntity<FeatureResult> handleScatterSearch(String datasetName) throws Exception;

    ResponseEntity<FeatureResult> handleRanker(String datasetName) throws Exception;

    ResponseEntity<FeatureResult> handleBestFirst(String datasetName) throws Exception;

    ResponseEntity<FeatureResult> handleExhaustive(String datasetName) throws Exception;

    ResponseEntity<FeatureResult> handleVNS(String datasetName) throws Exception;

    ResponseEntity<FeatureResult> applyFCBF(String name, Instances trainingData) throws Exception;

    ResponseEntity<FeatureResult> applyScatterSearch(String fileName, Instances trainingData) throws Exception;

    ResponseEntity<FeatureResult> applyRanker(String fileName, Instances trainingData) throws Exception;

    ResponseEntity<FeatureResult> applyBestFirst(String fileName, Instances trainingData) throws Exception;

    ResponseEntity<FeatureResult> applyExhaustive(String fileName, Instances trainingData) throws Exception;

    ResponseEntity<FeatureResult> applyVNS(String filename, ClassificationDataset dataset) throws Exception;


    List<FeatureResult> getResultsByUser(Authentication authentication) throws Exception;
    List<FeatureResult> getNewResultsByUser(Authentication authentication) throws Exception;
    void setResultSeen(FeatureResult featureResult) throws Exception;

    List<Dataset> datasetsByUser(Authentication authentication) throws Exception;


}
