package es.kazzpa.selattserver.services;


import es.kazzpa.selattserver.models.*;
import es.kazzpa.selattserver.repositories.DatasetRepository;
import es.kazzpa.selattserver.repositories.ResultRepository;
import org.springframework.stereotype.Service;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.CfsSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.attributeSelection.PrincipalComponents;
import weka.attributeSelection.Ranker;
import weka.attributeSelection.FCBFSearch;
import weka.attributeSelection.ScatterSearchV1;




import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.RandomProjection;
import weka.filters.unsupervised.attribute.Remove;

import java.io.FileWriter;

@Service("featureSelectionService")
public class FeatureSelectionServiceImpl implements FeatureSelectionService {

    private final FileFactory fileFactory;
    private final ResultRepository resRepo;
    private final DatasetRepository dataRepo;
    private final LoadData loadData;

    public FeatureSelectionServiceImpl(FileFactory fileFactory, ResultRepository resRepo, DatasetRepository dataRepo, LoadData loadData) {
        this.fileFactory = fileFactory;
        this.resRepo = resRepo;
        this.dataRepo = dataRepo;
        this.loadData = loadData;
    }

    public void handleLoadDefaultData() {
        loadData.loadDefaultDataBase();
    }

    public ResultFilter handlePCAFeatures() throws Exception {
        FileFactory.TrainTest carTrainTest = fileFactory.getInstancesFromFile(ML.Files.Car, new Options());
        FileFactory.TrainTest censusTrainTest = fileFactory.getInstancesFromFile(ML.Files.Census, new Options());
        return ApplyPCA("CAR", carTrainTest.train);
    }

    //Pca from database file
    public ResultFilter handlePCAFeatures(String datasetName) throws Exception {
        Dataset data = dataRepo.findDatasetByName(datasetName);
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

    /** TODO: FIX IMPORT FCBF
     * public ResultFilter ApplyFCBF(String name, Instances trainingData) throws Exception{
     * AttributeSelection selector = new AttributeSelection();
     * <p>
     * }
     */
    public ResultFilter ApplyPCA(String name, Instances trainingData) throws Exception {
        AttributeSelection selector = new AttributeSelection();

        PrincipalComponents principalComponents = new PrincipalComponents();
        principalComponents.setMaximumAttributeNames(5);
        principalComponents.setVarianceCovered(.95);
        principalComponents.buildEvaluator(trainingData);

        Ranker ranker = new Ranker();

        selector.setSearch(ranker);
        selector.setEvaluator(principalComponents);
        selector.SelectAttributes(trainingData);
        ResultFilter rf = new ResultFilter();
        Algorithm a = new Algorithm("PCA");
        rf.setAlgorithm(a);
        Usuario user = new Usuario("Default");
        Dataset dat = new Dataset(user);
        return rf;
    }

    public Instances applyRP(Instances trainingData, int numAttributes) throws Exception {
        RandomProjection randomProjection = new RandomProjection();
        randomProjection.setNumberOfAttributes(numAttributes);
        randomProjection.setInputFormat(trainingData);
        return Filter.useFilter(trainingData, randomProjection);
    }

    public ResultFilter applyPCAFilter(Instances trainingData, int numAttributes) throws Exception {
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
        System.out.println("Size training: " + size + "\nSize trained\n");
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
                    System.out.println("Value " + i + ": " + ins1[i] + "\tValue " + j + ": " + ins2[j]);
                }
                j++;
            }

            i++;
        }


        ResultFilter rf = new ResultFilter();
        Algorithm a = new Algorithm("PCA-Filter");
        rf.setAlgorithm(a);
        Usuario user = new Usuario("Default");
        Dataset dat = new Dataset(user);
        resRepo.save(rf);
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

    public ResultFilter plotPCA() throws Exception {
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