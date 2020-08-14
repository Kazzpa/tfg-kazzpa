package es.kazzpa.selattserver.services;


import es.kazzpa.selattserver.models.*;
import es.kazzpa.selattserver.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


import upo.jcu.math.set.Subset;
import weka.attributeSelection.*;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.RandomProjection;
import weka.filters.unsupervised.attribute.Remove;

import upo.jcu.math.data.dataset.DataType;
import upo.jml.data.dataset.ClassificationDataset;
import upo.jml.data.dataset.DatasetUtils;
import upo.jml.prediction.classification.fss.algorithms.FSPredGroupsBasicVNS;
import upo.jml.prediction.classification.fss.core.FSObjectiveFunction;
import upo.jml.prediction.classification.fss.core.FSSolution;
import upo.jml.prediction.classification.fss.evaluators.CfsEvaluator;

import java.io.FileWriter;
import java.util.*;

@Service("featureSelectionService")
public class FeatureSelectionServiceImpl implements FeatureSelectionService {

    private final FileFactory fileFactory;
    @Autowired
    private final FeatureResultRepository featureRepo;
    @Autowired
    private final DatasetRepository dataRepo;
    @Autowired
    private final AlgorithmRepository algoRepo;
    @Autowired
    private final AppUserRepository userRepo;
    private final LoadData loadData;

    public FeatureSelectionServiceImpl(FileFactory fileFactory, DatasetRepository dataRepo, LoadData loadData, AlgorithmRepository algoRepo, AppUserRepository userRepo, FeatureResultRepository resRepo) {
        this.fileFactory = fileFactory;
        this.featureRepo = resRepo;
        this.dataRepo = dataRepo;
        this.loadData = loadData;
        this.algoRepo = algoRepo;
        this.userRepo = userRepo;
    }

    public void handleLoadDefaultData() {
        loadData.loadDefaultDataBase();
    }

    public FeatureResult handlePCAFeatures() throws Exception {
        FileFactory.TrainTest carTrainTest = fileFactory.getInstancesFromFile(ML.Files.Car, new Options());
        FileFactory.TrainTest censusTrainTest = fileFactory.getInstancesFromFile(ML.Files.Census, new Options());
        return ApplyPCA("CAR", carTrainTest.train);
    }

    public ResponseEntity<FeatureResult> handleVNS(String datasetName) throws Exception {

        ClassificationDataset dataset = loadData.getClassDatasetFromArff(datasetName);
        if (!dataset.getDataType().equals(DataType.CATEGORICAL)) {
            dataset = DatasetUtils.dicretizeViaFayyad(dataset);
        }

        return applyVNS(datasetName, dataset);
    }

    public ResponseEntity<FeatureResult> handleFCBF(String datasetName) throws Exception {
        Instances trainingData = loadData.getInstancesFromAnyFile(datasetName);
        return applyFCBF(datasetName, trainingData);
    }

    public ResponseEntity<FeatureResult> handleScatterSearch(String datasetName) throws Exception {
        Instances trainingData = loadData.getInstancesFromAnyFile(datasetName);
        return applyScatterSearch(datasetName, trainingData);
    }

    @Override
    public List<FeatureResult> resultsByUser(Authentication authentication) throws Exception {
        String username = authentication.getName();
        AppUser appUser = userRepo.findByUsername(username);
        List<FeatureResult> ret = featureRepo.findFeatureResultByPerformed_UserUploader(appUser);
        for (FeatureResult r : ret
        ) {
            r.getPerformed().setUserUploader(null);
        }
        return ret;
    }

    @Override
    public List<Dataset> datasetsByUser(Authentication authentication) throws Exception {
        String username = authentication.getName();
        AppUser appUser = userRepo.findByUsername(username);
        List<Dataset> ret = dataRepo.findDatasetByUserUploader(appUser);
        //make the user information not seen in frontend
        for (Dataset d : ret
        ) {
            d.setUserUploader(null);
        }
        return ret;
    }

    public FeatureResult handlePCAFeatures(String datasetName) throws Exception {
        Dataset data = dataRepo.findDatasetByFilename(datasetName);
        FileFactory.TrainTest dataTrainTest = fileFactory.getInstancesFromFile(data.getFilename(), data.getFileDownloadUri(), new Options());
        return ApplyPCA("datasetName", dataTrainTest.train);
    }

    public String handleRandomizedProjectionFeatures() throws Exception {
        FileFactory.TrainTest carTrainTest = fileFactory.getInstancesFromFile(ML.Files.Car, new Options());
        FileFactory.TrainTest censusTrainTest = fileFactory.getInstancesFromFile(ML.Files.Census, new Options());
        return applyRP(carTrainTest.train, 4).toString() + "\n \n \n \n \n" + applyRP(censusTrainTest.train, 4).toString();
    }

    public String handleCFSSubsetEval() throws Exception {
        FileFactory.TrainTest carTrainTest = fileFactory.getInstancesFromFile(ML.Files.Car, new Options());
        FileFactory.TrainTest censusTrainTest = fileFactory.getInstancesFromFile(ML.Files.Census, new Options());
        return applyCfsSubsetEval(carTrainTest.train) + " \n \n \n \n" + applyCfsSubsetEval(censusTrainTest.train);
    }


    public ResponseEntity<FeatureResult> applyFCBF(String datasetName, Instances trainingData) throws Exception {

        //Check if the resultfilter has been processed before
        Algorithm fcbf = loadData.getAlgorithm("FastCorrelationBasedFilter", "Weka package");
        Dataset dataset = loadData.getDataset(datasetName);
        FeatureResult rf = loadData.checkIfFeatureAlreadyExists(fcbf, dataset);
        if (rf.getFinishedDate() != null) {
            return ResponseEntity.ok(rf);
        }
        //fcbf heuristico : no generico, cerrado la estrategia de busqueda y evaluacion
        //utiliza: simuncertAttributeSetEval
        SymmetricalUncertAttributeSetEval eval = new SymmetricalUncertAttributeSetEval();
        //default class index to last att if undefined
        //https://weka.sourceforge.io/doc.dev/weka/core/Instances.html
        if (trainingData.classIndex() < 0) {
            trainingData.setClassIndex(trainingData.numAttributes() - 1);
        }
        eval.buildEvaluator(trainingData);
        FCBFSearch fcbfSearch = new FCBFSearch();
        int[] sol = fcbfSearch.search(eval, trainingData);
        rf = loadData.saveFeatureSelectionResult(rf,sol);
        return ResponseEntity.ok(rf);

    }

    public ResponseEntity<FeatureResult> applyScatterSearch(String datasetName, Instances trainingData) throws Exception {

        Algorithm scatterSearch = loadData.getAlgorithm("ScatterSearchV1", "Weka package");
        Dataset dataset = loadData.getDataset(datasetName);
        FeatureResult fr = loadData.checkIfFeatureAlreadyExists(scatterSearch, dataset);

        if (fr.getFinishedDate() != null) {
            return ResponseEntity.ok(fr);
        }

        CfsSubsetEval eval = new CfsSubsetEval();
        //default class index to last att if undefined
        //https://weka.sourceforge.io/doc.dev/weka/core/Instances.html
        if (trainingData.classIndex() < 0) {
            trainingData.setClassIndex(trainingData.numAttributes() - 1);
        }
        eval.buildEvaluator(trainingData);

        ScatterSearchV1 scatterSearchV1 = new ScatterSearchV1();
        int[] sol = scatterSearchV1.search(eval, trainingData);
        fr = loadData.saveFeatureSelectionResult(fr,sol);
        return ResponseEntity.ok(fr);
    }

    public ResponseEntity<FeatureResult> applyVNS(String datasetName, ClassificationDataset dataset) throws Exception {
        //ClassificationDataset ddataset = com.jscilib.math.data.dataset.DatasetUtils.dicretizeViaFayyad(dataset);
        //logger.info(ddataset.toString());

        Algorithm vns = loadData.getAlgorithm("VariableNeighbourhoodSearch", "Paper published");
        Dataset dataset1 = loadData.getDataset(datasetName);
        FeatureResult rf = loadData.checkIfFeatureAlreadyExists(vns, dataset1);
        if (rf.getFinishedDate() != null) {
            Dataset aux = rf.getPerformed();
            aux.setUserUploader(null);
            rf.setPerformed(aux);
            return ResponseEntity.ok(rf);
        }
        FSObjectiveFunction of = new CfsEvaluator(dataset.getCategoricalData(), dataset.getLabels());
        of.buildEvaluator();
        FSPredGroupsBasicVNS bvns = new FSPredGroupsBasicVNS(dataset.getCategoricalData(), dataset.getLabels(), of, true);
        FSSolution solution = bvns.search();
        Subset<Integer> sol = solution.getSubset();
        Iterator it = sol.elements();
        List<Integer> lista = new ArrayList<Integer>();

        while (it.hasNext()) {
            int next = (int) it.next();
            lista.add(next);
        }
        int[] solArr = new int[lista.size()];
        for (int i = 0; i < solArr.length; i++) {
            solArr[i] = lista.get(i);
        }

        rf = loadData.saveFeatureSelectionResult(rf,solArr);
        return ResponseEntity.ok(rf);
    }

    public FeatureResult ApplyPCA(String name, Instances trainingData) throws Exception {
        AttributeSelection selector = new AttributeSelection();

        PrincipalComponents principalComponents = new PrincipalComponents();
        principalComponents.setMaximumAttributeNames(5);
        principalComponents.setVarianceCovered(.95);
        principalComponents.buildEvaluator(trainingData);

        Ranker ranker = new Ranker();

        selector.setSearch(ranker);
        selector.setEvaluator(principalComponents);
        selector.SelectAttributes(trainingData);
        FeatureResult rf = new FeatureResult();
        Algorithm a = new Algorithm("PCA");
        rf.setAlgorithm(a);
        return rf;
    }

    public Instances applyRP(Instances trainingData, int numAttributes) throws Exception {
        RandomProjection randomProjection = new RandomProjection();
        randomProjection.setNumberOfAttributes(numAttributes);
        randomProjection.setInputFormat(trainingData);
        return Filter.useFilter(trainingData, randomProjection);
    }

    public FeatureResult applyPCAFilter(Instances trainingData, int numAttributes) throws Exception {
        weka.filters.unsupervised.attribute.PrincipalComponents principalComponents = new weka.filters.unsupervised.attribute.PrincipalComponents();
        principalComponents.setMaximumAttributes(numAttributes);
        principalComponents.setInputFormat(trainingData);
        Instances trainedData = Filter.useFilter(trainingData, principalComponents);
        //Comparar los dataset una vez filtrado y sin filtrar
        //Guardaremos la lista de los atributos

        int ind = 0;
        double[] ins1 = trainedData.get(1).toDoubleArray();
        double[] ins2 = trainingData.get(1).toDoubleArray();
        int size = trainingData.numAttributes();
        int size2 = trainedData.numAttributes();
        int[] list = new int[trainedData.numAttributes()];
        int i = 0;
        int j = 0;
        while (i < size2) {
            boolean enc = false;
            while (j < size && !enc) {
                if (ins1[i] == ins2[j]) {
                    list[i] = j;
                    i++;
                    enc = true;
                } else {
                }
                j++;
            }

            i++;
        }


        FeatureResult rf = new FeatureResult();
        Algorithm a = new Algorithm("PCA-Filter");
        rf.setAlgorithm(a);
        featureRepo.save(rf);
        return rf;
    }

    public String applyCfsSubsetEval(Instances data) throws Exception {
        AttributeSelection selector = new AttributeSelection();
        CfsSubsetEval cfsSubsetEval = new CfsSubsetEval();
        cfsSubsetEval.buildEvaluator(data);

        GreedyStepwise greedyStepwise = new GreedyStepwise();
        greedyStepwise.setGenerateRanking(true);
        greedyStepwise.setNumToSelect(4);
        selector.setSearch(greedyStepwise);
        selector.setEvaluator(cfsSubsetEval);
        selector.SelectAttributes(data);

        return " \n \n Principal Components: \n \n "
                + cfsSubsetEval.toString() + "\n \n Attribute Selection: \n \n" + selector.toResultsString();
    }

    private void plotRP(String fileName, Instances data) throws Exception {
        FileWriter writer = new FileWriter(fileName + ".csv", true);
        for (int i = 0; i < data.size(); i++) {
            double[] values = data.get(i).toDoubleArray();
            writer.append(new Double(values[0]).toString());
            writer.append(",");
            writer.append(new Double(values[1]).toString());
            writer.append(",");
            writer.append(new Double(data.get(i).classValue()).toString());
            writer.append("\n");
        }
        writer.flush();
        writer.close();
    }

    public void plotRP() throws Exception {
        FileFactory.TrainTest carTrainTest = fileFactory.getInstancesFromFile(ML.Files.Car, new Options());
        FileFactory.TrainTest censusTrainTest = fileFactory.getInstancesFromFile(ML.Files.Census, new Options());
        plotRP("RP_car", applyRP(carTrainTest.train, 2));
        plotRP("RP_census", applyRP(censusTrainTest.train, 2));
    }

    public FeatureResult plotPCA() throws Exception {
        FileFactory.TrainTest carTrainTest = fileFactory.getInstancesFromFile(ML.Files.Car, new Options());
        //FileFactory.TrainTest censusTrainTest = fileFactory.getInstancesFromFile(ML.Files.Census, new Options());
        return applyPCAFilter(carTrainTest.train, 2);
        //plotRP("PCA_car", applyPCAFilter(carTrainTest.train, 2));
        //plotRP("PCA_census", applyPCAFilter(censusTrainTest.train, 2));
    }


    private Instances dropClass(Instances instances) throws Exception {
        Remove removeFilter = new Remove();
        String[] options = new String[]{"-R", Integer.toString(instances.numAttributes() - 1)};
        removeFilter.setOptions(options);
        removeFilter.setInputFormat(instances);
        return Filter.useFilter(instances, removeFilter);
    }

    public Instances reAddClassification(Instances first, Instances second) throws Exception {
        Add filter = new Add();
        filter.setAttributeIndex("last");
        filter.setAttributeName("NewNumeric");
        filter.setInputFormat(first);
        Instances newFirst = Filter.useFilter(first, filter);
        for (int i = 0; i < newFirst.size(); i++) {
            newFirst.instance(i).setValue(newFirst.numAttributes() - 1, second.instance(i).classValue());
            newFirst.setClassIndex(newFirst.numAttributes() - 1);
        }
        return newFirst;
    }

    public Instances reAddClassificationNominal(Instances first, Instances second) throws Exception {
        Add filter = new Add();
        filter.setAttributeIndex("last");
        filter.setNominalLabels("0,1");
        filter.setAttributeName("NewNumeric");
        filter.setInputFormat(first);
        Instances newFirst = Filter.useFilter(first, filter);

        for (int i = 0; i < newFirst.size(); i++) {
            newFirst.instance(i).setValue(newFirst.numAttributes() - 1, second.instance(i).classValue());
            newFirst.setClassIndex(newFirst.numAttributes() - 1);
        }
        return newFirst;
    }





}