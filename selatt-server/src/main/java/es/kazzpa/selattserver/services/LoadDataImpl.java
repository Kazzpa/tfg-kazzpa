package es.kazzpa.selattserver.services;

import es.kazzpa.selattserver.models.*;
import es.kazzpa.selattserver.properties.Properties;
import es.kazzpa.selattserver.repositories.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import upo.jml.data.dataset.ClassificationDataset;
import upo.jml.data.dataset.DatasetUtils;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.JSONLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service("loadData")
public class LoadDataImpl implements LoadData {
    @Autowired
    private ResultRepository resultRepo;
    @Autowired
    private AlgorithmRepository algoRepo;
    @Autowired
    private DatasetRepository dataRepo;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Path fileStorageLocation;

    @Autowired
    public LoadDataImpl(Properties properties) {
        this.fileStorageLocation = Paths.get(properties.getUploadDir())
                .toAbsolutePath()
                .normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);

        } catch (Exception ex) {
            System.out.println("Error al crear el directorio" + fileStorageLocation);
        }
    }

    //this method is run for test purposes
    public void loadDefaultDataBase() {

        AppUser kazzpa = new AppUser("kazzpa", passwordEncoder.encode("kazzpa"), "USER");
        AppUser user = appUserRepository.findByUsername(kazzpa.getUsername());
        if (user == null) {
            appUserRepository.save(kazzpa);
        }
        AppUser admin = new AppUser("admin", passwordEncoder.encode("kazzpa"), "ADMIN");

        user = appUserRepository.findByUsername(admin.getUsername());

        if (user == null) {
            appUserRepository.save(admin);
        }

    }

    public Instances getInstancesFromCsvFile(String fileName) throws Exception {
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(fileStorageLocation + "/" + fileName));
        return loader.getDataSet();
    }

    public void saveToArff(Instances instances, String fileName) throws IOException {
        ArffSaver arffSaver = new ArffSaver();
        arffSaver.setInstances(instances);
        arffSaver.setFile(new File("src/main/resources/" + fileName));
        arffSaver.setDestination(new File("src/main/resources/arffs/" + fileName));
        arffSaver.writeBatch();
    }

    public Instances getInstancesFromArff(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileStorageLocation + "/" + fileName));
        ArffLoader.ArffReader arff = new ArffLoader.ArffReader(reader, 100000);
        Instances data = arff.getStructure();
        data.setClassIndex(data.numAttributes() - 1);
        Instance inst;
        while ((inst = arff.readInstance(data)) != null) {
            data.add(inst);
        }
        return data;
    }


    public Instances getInstancesFromArff(String fileName, boolean noClass) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileStorageLocation + "/" + fileName));
        ArffLoader.ArffReader arff = new ArffLoader.ArffReader(reader, 100000);
        Instances data = arff.getStructure();
        if (!noClass) {
            data.setClassIndex(data.numAttributes() - 1);
        }
        Instance inst;
        while ((inst = arff.readInstance(data)) != null) {
            data.add(inst);
        }
        return data;
    }

    //TODO: Json loading fails java.lang.Exception: Can't recover from previous error(s)
    public Instances getInstancesFromJson(String fileName) throws Exception {
        try {
            JSONLoader jsonLoader = new JSONLoader();
            File file = new File(fileStorageLocation + "/" + fileName);
            jsonLoader.setSource(file);
            return jsonLoader.getDataSet();
        } catch (IOException ex) {
            throw new Exception("Archivo no encontrado en:" + fileStorageLocation + "/" + fileName + ex.getMessage());
        } catch (NullPointerException ex) {
            throw new Exception("Archivo not found" + ex.getMessage());
        } catch (Exception ex) {
            throw new Exception("Error en " + ex.getMessage());
        }
    }

    public Instances getInstancesFromAnyFile(String fileName) throws Exception {
        try {
            //TODO: FIX LOADING FROM .JSON AND .ARFF
            File file = new File(fileStorageLocation + "/" + fileName);
            String mimeType = new Tika().detect(file);

            switch (mimeType) {
                case "text/csv":
                    return getInstancesFromCsvFile(fileName);
                case "application/json":
                    return getInstancesFromJson(fileName);
                default:
                    String ext = FilenameUtils.getExtension(fileName);
                    if (ext.equals("arff")) {
                        return getInstancesFromArff(fileName);
                    } else {
                        throw new Exception("Unsupported File Type : " + fileName + " Extension:" + ext);
                    }
            }
        } catch (IOException ex) {
            throw new Exception("File not found:" + fileName + " " + ex.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    public ClassificationDataset getClassDatasetFromArff(String filename) throws Exception {
        System.out.println(fileStorageLocation + "/" + filename);
        return DatasetUtils.loadArffDataset(new File(fileStorageLocation + "/" + filename), -1);
    }

    public void saveModel(Classifier cls, String name) throws Exception {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("src/main/resources/models/" + name));
        objectOutputStream.writeObject(cls);
        objectOutputStream.flush();
        objectOutputStream.close();
    }

    public Classifier getModel(String name) throws Exception {
        ObjectInputStream oos = new ObjectInputStream(new FileInputStream("src/main/resources/models/" + name));
        Classifier cls = (Classifier) oos.readObject();
        oos.close();
        return cls;

    }
}
