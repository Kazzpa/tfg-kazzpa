package es.kazzpa.selattserver.services;

import upo.jml.data.dataset.ClassificationDataset;
import weka.core.Instances;

import java.io.IOException;

public interface LoadData {
    void loadDefaultDataBase();
    void saveToArff(Instances instances, String fileName) throws IOException;
    Instances getInstancesFromAnyFile(String fileName) throws Exception;
    ClassificationDataset getClassDatasetFromArff(String filename) throws Exception;
}
