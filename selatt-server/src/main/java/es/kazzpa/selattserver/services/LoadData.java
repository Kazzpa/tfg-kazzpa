package es.kazzpa.selattserver.services;

import weka.core.Instances;

import java.io.IOException;

public interface LoadData {
    void loadDefaultDataBase();
    Instances getDataFromCsvFile(String filename) throws Exception;
    void saveToArff(Instances instances, String fileName) throws IOException;
    Instances getDataFromArff(String fileName) throws IOException;
    Instances getDataFromArff(String fileName, boolean noClass) throws IOException;
}
