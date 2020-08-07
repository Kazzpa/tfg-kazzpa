package es.kazzpa.selattserver.services;

import es.kazzpa.selattserver.models.Algorithm;
import es.kazzpa.selattserver.models.Dataset;
import es.kazzpa.selattserver.models.ResultFilter;
import upo.jml.data.dataset.ClassificationDataset;
import weka.core.Instances;

import java.io.IOException;

public interface LoadData {
    void loadDefaultDataBase();

    void saveToArff(Instances instances, String fileName) throws IOException;

    Instances getInstancesFromAnyFile(String fileName) throws Exception;

    ClassificationDataset getClassDatasetFromArff(String filename) throws Exception;

    ResultFilter checkIfAlreadyExists(Algorithm algorithm, Dataset dataset) throws Exception;

    ResultFilter saveResultFilter(int[] solution, ResultFilter rf, Algorithm algorithm, Dataset dataset) throws Exception;

    ResultFilter saveResultFilter(String summary, ResultFilter rf, Algorithm algorithm, Dataset dataset) throws Exception;

    Dataset getDataset(String datasetName) throws Exception;

    Algorithm getAlgorithm(String algorithm, String language) throws Exception;
}
