package es.kazzpa.selattserver.services;

import es.kazzpa.selattserver.models.*;
import es.kazzpa.selattserver.properties.Properties;
import es.kazzpa.selattserver.repositories.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import upo.jml.data.dataset.ClassificationDataset;
import upo.jml.data.dataset.DatasetUtils;
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
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service("loadData")
public class LoadDataImpl implements LoadData {
    @Autowired
    private ClassifierResultRepository resultRepo;
    @Autowired
    private AlgorithmRepository algoRepo;
    @Autowired
    private DatasetRepository dataRepo;
    @Autowired
    private ClassifierResultRepository clasRepo;
    @Autowired
    private FeatureResultRepository featureRepo;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Path fileStorageLocation;

    @Autowired
    public LoadDataImpl(Properties properties) {
        this.fileStorageLocation = Paths.get(properties.getUploadDir())
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
        loader.setSource(new File(fileName));
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
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
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
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
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
            File file = new File(fileName);
            jsonLoader.setSource(file);
            return jsonLoader.getDataSet();
        } catch (IOException ex) {
            throw new Exception("Archivo no encontrado en:" + fileName + ex.getMessage());
        } catch (NullPointerException ex) {
            throw new Exception("Archivo not found" + ex.getMessage());
        } catch (Exception ex) {
            throw new Exception("Error en " + ex.getMessage());
        }
    }

    public Instances getInstancesFromAnyFile(String fileName) throws Exception {
        Dataset dat = null;
        try {
            //TODO: FIX LOADING FROM .JSON AND .ARFF
            dat = dataRepo.findDatasetByFilename(fileName);
            String filePath = System.getProperty("user.dir") + dat.getFileDownloadUri();
            Path path = this.fileStorageLocation.resolve(StringUtils.cleanPath(filePath));
            File file = path.toFile();
            String mimeType = new Tika().detect(file);

            switch (mimeType) {
                case "text/csv":
                    return getInstancesFromCsvFile(path.toString());
                case "application/json":
                    return getInstancesFromJson(path.toString());
                default:
                    String ext = FilenameUtils.getExtension(path.toString());
                    if (ext.equals("arff")) {
                        return getInstancesFromArff(path.toString());
                    } else {
                        throw new Exception("Unsupported File Type : " + fileName + " Extension:" + ext);
                    }
            }
        } catch (IOException | NullPointerException ex) {
            System.out.println(System.getProperty("user.dir") + dat.getFileDownloadUri());
            return null;
        }
    }


    public ClassificationDataset getClassDatasetFromArff(String fileName) throws Exception {
        Dataset dat = dataRepo.findDatasetByFilename(fileName);
        String filePath = System.getProperty("user.dir") + dat.getFileDownloadUri();
        Path path = this.fileStorageLocation.resolve(StringUtils.cleanPath(filePath));
        return DatasetUtils.loadArffDataset(new File(path.toString()), -1);
    }


    public ClassifierResult checkIfClassifierResultAlreadyExists(Algorithm algorithm, Dataset dataset, FeatureResult feature) throws Exception {
        ClassifierResult rf = new ClassifierResult();
        rf.setAlgorithm(algorithm);
        rf.setPerformed(dataset);
        if (feature == null) {
            ClassifierResult alreadyPerformed =
                    clasRepo.findClassifierResultByPerformedAndAlgorithmAndFeatureAlgorithmIsNull(dataset, algorithm);
            if (alreadyPerformed != null) {
                return alreadyPerformed;
            }
        } else {
            ClassifierResult alreadyPerformed =
                    clasRepo.findClassifierResultByPerformedAndAlgorithmAndFeatureAlgorithm(dataset, algorithm, feature.getAlgorithm());
            if (alreadyPerformed != null) {
                return alreadyPerformed;
            }
        }
        return rf;
    }

    public FeatureResult checkIfFeatureAlreadyExists(Algorithm algorithm, Dataset dataset) throws Exception {
        FeatureResult rf = new FeatureResult();
        rf.setAlgorithm(algorithm);
        rf.setPerformed(dataset);
        FeatureResult alreadyPerformed = featureRepo.findByAlgorithmAndPerformed(algorithm, dataset);
        if (alreadyPerformed != null) {
            return alreadyPerformed;
        }
        return rf;
    }

    public ClassifierResult saveClassifierResult(ClassifierResult rf) throws Exception {
        Date now = new Date();
        rf.setFinishedDate(now);
        clasRepo.save(rf);
        Dataset performed = rf.getPerformed();
        performed.setUserUploader(null);
        rf.setPerformed(performed);
        return rf;

    }

    public FeatureResult saveFeatureSelectionResult(FeatureResult fsr, int[] solution) throws Exception {
        Arrays.sort(solution);
        String result = IntStream.of(solution)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(", "));
        fsr.setAttributesSelected(result);
        Date now = new Date();
        fsr.setFinishedDate(now);
        fsr.setNumAttributes(solution.length);
        featureRepo.save(fsr);
        Dataset performed = fsr.getPerformed();
        performed.setUserUploader(null);
        fsr.setPerformed(performed);
        return fsr;

    }

    public Dataset getDataset(String datasetName) throws Exception {

        Dataset dataset = dataRepo.findDatasetByFilename(datasetName);
        if (dataset == null) {
            throw new Exception(datasetName + " Not found in DB");
        }
        return dataset;
    }

    public Algorithm getAlgorithm(String algorithm_name, String language, String type) throws Exception {
        Algorithm algorithm = algoRepo.findAlgorithmByName(algorithm_name);
        if (algorithm == null) {
            algorithm = new Algorithm();
            algorithm.setLanguage(language);
            algorithm.setName(algorithm_name);
            algorithm.setType(type);
            algoRepo.save(algorithm);
        }
        return algorithm;
    }
}
