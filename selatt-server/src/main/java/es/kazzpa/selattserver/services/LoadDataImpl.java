package es.kazzpa.selattserver.services;

import es.kazzpa.selattserver.models.*;
import es.kazzpa.selattserver.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Attr;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.*;

@Service("loadData")
public class LoadDataImpl implements LoadData {
    @Autowired
    private ResultRepository resultRepo;
    @Autowired
    private AlgorithmRepository algoRepo;
    @Autowired
    private AttributeRepository attRepo;
    @Autowired
    private DatasetRepository dataRepo;
    @Autowired
    private UserRepository userRepo;

    //this method is run for test purposes
    public void loadDefaultDataBase(){
        Usuario user = new Usuario();
        user.setNombre("Andres");
        userRepo.save(user);

        Dataset data = new Dataset();
        data.setName("Covid-19");
        data.setFileDownloadUri("C:/hola.txt");
        data.setUploader(user);
        dataRepo.save(data);

        Algorithm pca = new Algorithm();
        pca.setLanguage("Java");
        pca.setName("Principal Componentes Analysis");
        algoRepo.save(pca);

        ResultFilter rf = new ResultFilter();
        rf.setAlgorithm(pca);
        rf.setPerformed(data);
        resultRepo.save(rf);

        Attribute uno = new Attribute();
        uno.setDataPerformed(rf);
        uno.setIndex(3);
        uno.setValue(20.2);
        attRepo.save(uno);

        Attribute dos = new Attribute();
        dos.setDataPerformed(rf);
        dos.setIndex(5);
        dos.setValue(1.2);
        attRepo.save(dos);

    }

    public Instances getDataFromCsvFile(String fileName) throws Exception {
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File("src/main/resources/csv/" + fileName));
        return loader.getDataSet();
    }

    public void saveToArff(Instances instances, String fileName) throws IOException {
        ArffSaver arffSaver = new ArffSaver();
        arffSaver.setInstances(instances);
        arffSaver.setFile(new File("src/main/resources/" + fileName));
        arffSaver.setDestination(new File("src/main/resources/arffs/" + fileName));
        arffSaver.writeBatch();
    }

    public Instances getDataFromArff(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/arffs/" + fileName));
        ArffLoader.ArffReader arff = new ArffLoader.ArffReader(reader, 100000);
        Instances data = arff.getStructure();
        data.setClassIndex(data.numAttributes() - 1);
        Instance inst;
        while ((inst = arff.readInstance(data)) != null){
            data.add(inst);
        }
        return data;
    }

    public Instances getDataFromArff(String fileName, boolean noClass) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/arffs/" + fileName));
        ArffLoader.ArffReader arff = new ArffLoader.ArffReader(reader, 100000);
        Instances data = arff.getStructure();
        if (!noClass){
            data.setClassIndex(data.numAttributes() - 1);
        }
        Instance inst;
        while ((inst = arff.readInstance(data)) != null){
            data.add(inst);
        }
        return data;
    }

    public void saveModel(Classifier cls, String name) throws Exception{
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("src/main/resources/models/" + name));
        objectOutputStream.writeObject(cls);
        objectOutputStream.flush();
        objectOutputStream.close();
    }

    public Classifier getModel(String name) throws Exception{
        ObjectInputStream oos = new ObjectInputStream(new FileInputStream("src/main/resources/models/" + name));
        Classifier cls = (Classifier)oos.readObject();
        oos.close();
        return cls;

    }
}