package es.kazzpa.selattserver.services;

import upo.jml.data.dataset.ClassificationDataset;
import weka.core.Instances;

import java.io.IOException;

public interface LoadData {
    void loadDefaultDataBase();
    Instances getDataFromCsvFile(String filename) throws Exception;
    void saveToArff(Instances instances, String fileName) throws IOException;
    Instances getDataFromArff(String fileName) throws IOException;
    Instances getDataFromArff(String fileName, boolean noClass) throws IOException;
    Instances getDataFromJson(String fileName) throws Exception;
    ClassificationDataset getDatasetFromArff(String filename) throws Exception;
}
