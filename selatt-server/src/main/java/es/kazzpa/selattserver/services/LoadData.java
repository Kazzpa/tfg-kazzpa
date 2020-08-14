package es.kazzpa.selattserver.services;

import es.kazzpa.selattserver.models.Algorithm;
import es.kazzpa.selattserver.models.Dataset;
import es.kazzpa.selattserver.models.ClassifierResult;
import es.kazzpa.selattserver.models.FeatureResult;
import upo.jml.data.dataset.ClassificationDataset;
import weka.core.Instances;

import java.io.IOException;

public interface LoadData {
    void loadDefaultDataBase();

    void saveToArff(Instances instances, String fileName) throws IOException;

    Instances getInstancesFromAnyFile(String fileName) throws Exception;

    ClassificationDataset getClassDatasetFromArff(String filename) throws Exception;

    ClassifierResult checkIfClassifierResultAlreadyExists(Algorithm algorithm, Dataset dataset, FeatureResult feature) throws Exception;
    FeatureResult checkIfFeatureAlreadyExists(Algorithm algorithm, Dataset dataset) throws Exception;

    FeatureResult saveFeatureSelectionResult(FeatureResult fsr, int [] solution) throws Exception;
    ClassifierResult saveClassifierResult( ClassifierResult rf) throws Exception;

    Dataset getDataset(String datasetName) throws Exception;

    Algorithm getAlgorithm(String algorithm, String language) throws Exception;
}
