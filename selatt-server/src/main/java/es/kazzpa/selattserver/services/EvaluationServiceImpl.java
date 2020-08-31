package es.kazzpa.selattserver.services;

import es.kazzpa.selattserver.models.*;
import es.kazzpa.selattserver.repositories.AlgorithmRepository;
import es.kazzpa.selattserver.repositories.AppUserRepository;
import es.kazzpa.selattserver.repositories.ClassifierResultRepository;
import es.kazzpa.selattserver.repositories.FeatureResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.HNB;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;
import weka.core.UnassignedClassException;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class EvaluationServiceImpl implements EvaluationService {

    @Autowired
    private final ClassifierResultRepository classifierRepo;
    @Autowired
    private final AppUserRepository userRepo;
    @Autowired
    private final FeatureResultRepository featureRepo;
    @Autowired
    private final AlgorithmRepository algoRepo;
    @Autowired
    private final LoadData loadData;

    public EvaluationServiceImpl(AlgorithmRepository algoRepo, FeatureResultRepository featureRepo, ClassifierResultRepository classifierRepo, LoadData loadData, AppUserRepository userRepo) {
        this.userRepo = userRepo;
        this.loadData = loadData;
        this.classifierRepo = classifierRepo;
        this.featureRepo = featureRepo;
        this.algoRepo = algoRepo;
    }

    @Override
    public ResponseEntity handleNaiveBayes(String datasetName) throws Exception {
        Instances trainingData = loadData.getInstancesFromAnyFile(datasetName);
        if (trainingData == null) {
            return ResponseEntity.notFound().build();
        }
        Algorithm naiveBayes = loadData.getAlgorithm("Naive Bayes", "Weka Package", "Classifier");
        Dataset dataset2 = loadData.getDataset(datasetName);
        ClassifierResult rf = loadData.checkIfClassifierResultAlreadyExists(naiveBayes, dataset2, null);
        if (rf.getFinishedDate() != null) {
            return ResponseEntity.ok(rf);
        }
        return applyNaiveBayes(rf, datasetName, trainingData);
    }

    @Override
    public ResponseEntity handleNaiveBayes(FeatureResult dataset) throws Exception {
        String datasetName = dataset.getPerformed().getFilename();
        String attributesSelected = dataset.getAttributesSelected();
        Instances trainingData = loadData.getInstancesFromAnyFile(datasetName);
        if (trainingData == null) {
            return ResponseEntity.notFound().build();
        }
        Instances newData = filterByAttributes(trainingData, attributesSelected);
        Algorithm naiveBayes = loadData.getAlgorithm("Naive Bayes", "Weka Package", "Classifier");
        Dataset dataset2 = loadData.getDataset(datasetName);
        ClassifierResult rf = loadData.checkIfClassifierResultAlreadyExists(naiveBayes, dataset2, dataset);
        if (rf.getFeatureAlgorithm() != null) {
            if (rf.getFeatureAlgorithm().getName().equals(dataset.getAlgorithm().getName())) {
                return ResponseEntity.ok(rf);
            }
        }
        ClassifierResult rf2 = new ClassifierResult();

        rf2.setFinishedDate(new Date());
        rf2.setAlgorithm(naiveBayes);
        rf2.setPerformed(dataset2);
        rf2.setFeatureAlgorithm(dataset.getAlgorithm());
        return applyNaiveBayes(rf2, datasetName, newData);
    }

    @Override
    public ResponseEntity<ClassifierResult> handleBayesNet(String datasetName) throws Exception {
        Instances trainingData = loadData.getInstancesFromAnyFile(datasetName);
        if (trainingData == null) {
            return ResponseEntity.notFound().build();
        }
        Algorithm bayesNet = loadData.getAlgorithm("Bayes Net", "Weka Package", "Classifier");
        Dataset dataset2 = loadData.getDataset(datasetName);
        ClassifierResult rf = loadData.checkIfClassifierResultAlreadyExists(bayesNet, dataset2, null);
        if (rf.getFinishedDate() != null) {
            return ResponseEntity.ok(rf);
        }
        return applyBayesNet(rf, datasetName, trainingData);
    }

    @Override
    public ResponseEntity<ClassifierResult> handleBayesNet(FeatureResult dataset) throws Exception {
        String datasetName = dataset.getPerformed().getFilename();
        String attributesSelected = dataset.getAttributesSelected();
        Instances trainingData = loadData.getInstancesFromAnyFile(datasetName);
        if (trainingData == null) {
            return ResponseEntity.notFound().build();
        }
        Instances newData = filterByAttributes(trainingData, attributesSelected);
        Algorithm bayesNet = loadData.getAlgorithm("Bayes Net", "Weka Package", "Classifier");
        Dataset dataset2 = loadData.getDataset(datasetName);
        ClassifierResult rf = loadData.checkIfClassifierResultAlreadyExists(bayesNet, dataset2, dataset);
        if (rf.getFeatureAlgorithm() != null) {
            if (rf.getFeatureAlgorithm().getName().equals(dataset.getAlgorithm().getName())) {
                return ResponseEntity.ok(rf);
            }
        }
        ClassifierResult rf2 = new ClassifierResult();

        rf2.setFinishedDate(new Date());
        rf2.setAlgorithm(bayesNet);
        rf2.setPerformed(dataset2);
        rf2.setFeatureAlgorithm(dataset.getAlgorithm());
        return applyNaiveBayes(rf2, datasetName, newData);
    }

    @Override
    public ResponseEntity<ClassifierResult> handleHiddenNaiveBayes(String datasetName) throws Exception {
        Instances trainingData = loadData.getInstancesFromAnyFile(datasetName);
        if (trainingData == null) {
            return ResponseEntity.notFound().build();
        }
        Algorithm bayesNet = loadData.getAlgorithm("Hidden Naive Bayes", "Weka Package", "Classifier");
        Dataset dataset2 = loadData.getDataset(datasetName);
        ClassifierResult rf = loadData.checkIfClassifierResultAlreadyExists(bayesNet, dataset2, null);
        if (rf.getFinishedDate() != null) {
            return ResponseEntity.ok(rf);
        }
        return applyHNB(rf, datasetName, trainingData);
    }

    @Override
    public ResponseEntity handleHiddenNaiveBayes(FeatureResult dataset) throws Exception {
        String datasetName = dataset.getPerformed().getFilename();
        String attributesSelected = dataset.getAttributesSelected();
        Instances trainingData = loadData.getInstancesFromAnyFile(datasetName);
        if (trainingData == null) {
            return ResponseEntity.notFound().build();
        }
        Instances newData = filterByAttributes(trainingData, attributesSelected);
        Algorithm HiddenNaiveBayes = loadData.getAlgorithm("Hidden Naive Bayes", "Weka Package", "Classifier");
        Dataset dataset2 = loadData.getDataset(datasetName);
        ClassifierResult rf = loadData.checkIfClassifierResultAlreadyExists(HiddenNaiveBayes, dataset2, dataset);
        if (rf.getFeatureAlgorithm() != null) {
            if (rf.getFeatureAlgorithm().getName().equals(dataset.getAlgorithm().getName())) {
                return ResponseEntity.ok(rf);
            }
        }
        ClassifierResult rf2 = new ClassifierResult();

        rf2.setFinishedDate(new Date());
        rf2.setAlgorithm(HiddenNaiveBayes);
        rf2.setPerformed(dataset2);
        rf2.setFeatureAlgorithm(dataset.getAlgorithm());
        return applyHNB(rf2, datasetName, newData);
    }

    @Override
    public ResponseEntity<ClassifierResult> handleIbk(String datasetName) throws Exception {
        Instances trainingData = loadData.getInstancesFromAnyFile(datasetName);
        if (trainingData == null) {
            return ResponseEntity.notFound().build();
        }
        Algorithm Ibk = loadData.getAlgorithm("IBk", "Weka Package", "Classifier");
        Dataset dataset2 = loadData.getDataset(datasetName);
        ClassifierResult rf = loadData.checkIfClassifierResultAlreadyExists(Ibk, dataset2, null);
        if (rf.getFinishedDate() != null) {
            return ResponseEntity.ok(rf);
        }
        return applyIbk(rf, datasetName, trainingData);
    }

    @Override
    public ResponseEntity<ClassifierResult> handleIbk(FeatureResult dataset) throws Exception {
        String datasetName = dataset.getPerformed().getFilename();
        String attributesSelected = dataset.getAttributesSelected();
        Instances trainingData = loadData.getInstancesFromAnyFile(datasetName);
        if (trainingData == null) {
            return ResponseEntity.notFound().build();
        }
        Instances newData = filterByAttributes(trainingData, attributesSelected);
        Algorithm Ibk = loadData.getAlgorithm("IBk", "Weka Package", "Classifier");
        Dataset dataset2 = loadData.getDataset(datasetName);
        ClassifierResult rf = loadData.checkIfClassifierResultAlreadyExists(Ibk, dataset2, dataset);
        if (rf.getFeatureAlgorithm() != null) {
            if (rf.getFeatureAlgorithm().getName().equals(dataset.getAlgorithm().getName())) {
                return ResponseEntity.ok(rf);
            }
        }
        ClassifierResult rf2 = new ClassifierResult();

        rf2.setFinishedDate(new Date());
        rf2.setAlgorithm(Ibk);
        rf2.setPerformed(dataset2);
        rf2.setFeatureAlgorithm(dataset.getAlgorithm());
        return applyIbk(rf2, datasetName, newData);
    }

    @Override
    public ResponseEntity<ClassifierResult> handleMlp(String datasetName) throws Exception {
        Instances trainingData = loadData.getInstancesFromAnyFile(datasetName);
        if (trainingData == null) {
            return ResponseEntity.notFound().build();
        }
        Algorithm mlp = loadData.getAlgorithm("MultiLayer Perceptron", "Weka Package", "Classifier");
        Dataset dataset2 = loadData.getDataset(datasetName);
        ClassifierResult rf = loadData.checkIfClassifierResultAlreadyExists(mlp, dataset2, null);
        if (rf.getFinishedDate() != null) {
            return ResponseEntity.ok(rf);
        }
        return applyMlp(rf, datasetName, trainingData);
    }

    @Override
    public ResponseEntity<ClassifierResult> handleMlp(FeatureResult dataset) throws Exception {
        String datasetName = dataset.getPerformed().getFilename();
        String attributesSelected = dataset.getAttributesSelected();
        Instances trainingData = loadData.getInstancesFromAnyFile(datasetName);
        if (trainingData == null) {
            return ResponseEntity.notFound().build();
        }
        Instances newData = filterByAttributes(trainingData, attributesSelected);
        Algorithm mlp = loadData.getAlgorithm("MultiLayer Perceptron", "Weka Package", "Classifier");
        Dataset dataset2 = loadData.getDataset(datasetName);
        ClassifierResult rf = loadData.checkIfClassifierResultAlreadyExists(mlp, dataset2, dataset);
        if (rf.getFeatureAlgorithm() != null) {
            if (rf.getFeatureAlgorithm().getName().equals(dataset.getAlgorithm().getName())) {
                return ResponseEntity.ok(rf);
            }
        }
        ClassifierResult rf2 = new ClassifierResult();

        rf2.setFinishedDate(new Date());
        rf2.setAlgorithm(mlp);
        rf2.setPerformed(dataset2);
        rf2.setFeatureAlgorithm(dataset.getAlgorithm());
        return applyMlp(rf2, datasetName, newData);
    }

    public Instances filterByAttributes(Instances trainingData, String attributesSelected) throws Exception {
        Remove removeFilter = new Remove();
        String[] attributes = attributesSelected.split(",");
        int[] indices = new int[attributes.length];
        for (int i = 0; i < attributes.length; ) {
            if (!attributes[i].trim().isEmpty()) {
                indices[i] = Integer.parseInt(attributes[i].trim());
            }
            i++;
        }
        removeFilter.setAttributeIndicesArray(indices);
        removeFilter.setInputFormat(trainingData);
        return Filter.useFilter(trainingData, removeFilter);
    }

    public ResponseEntity<ClassifierResult> applyNaiveBayes(ClassifierResult rf, String datasetName, Instances trainingData) throws Exception {
        NaiveBayes classifier = new NaiveBayes();
        try {
            if (trainingData.classIndex() == -1) {
                trainingData.setClassIndex(trainingData.numAttributes() - 1);
            }
        } catch (UnassignedClassException ex) {
            throw new Exception(ex.getMessage());
        }
        classifier.buildClassifier(trainingData);
        Evaluation eval = new Evaluation(trainingData);
        eval.crossValidateModel(classifier, trainingData, 10, new Random());

        rf.setNumInstances(eval.numInstances());
        rf.setCorrectlyClassified(eval.correct());
        rf.setMeanAbsoluteError(eval.meanAbsoluteError());
        loadData.saveClassifierResult(rf);
        return ResponseEntity.ok(rf);


    }

    public ResponseEntity<ClassifierResult> applyBayesNet(ClassifierResult rf, String datasetName, Instances trainingData) throws Exception {
        BayesNet classifier = new BayesNet();
        try {
            if (trainingData.classIndex() == -1) {
                trainingData.setClassIndex(trainingData.numAttributes() - 1);
            }
        } catch (UnassignedClassException ex) {
            throw new Exception(ex.getMessage());
        }
        classifier.buildClassifier(trainingData);
        Evaluation eval = new Evaluation(trainingData);
        eval.crossValidateModel(classifier, trainingData, 10, new Random());

        rf.setNumInstances(eval.numInstances());
        rf.setCorrectlyClassified(eval.correct());
        rf.setMeanAbsoluteError(eval.meanAbsoluteError());
        loadData.saveClassifierResult(rf);
        return ResponseEntity.ok(rf);
    }

    public ResponseEntity<ClassifierResult> applyHNB(ClassifierResult rf, String datasetName, Instances trainingData) throws Exception {

        HNB classifier = new HNB();
        try {
            if (trainingData.classIndex() == -1) {
                trainingData.setClassIndex(trainingData.numAttributes() - 1);
            }
        } catch (UnassignedClassException ex) {
            throw new Exception(ex.getMessage());
        }
        classifier.buildClassifier(trainingData);
        Evaluation eval = new Evaluation(trainingData);
        eval.crossValidateModel(classifier, trainingData, 10, new Random());

        rf.setNumInstances(eval.numInstances());
        rf.setCorrectlyClassified(eval.correct());
        rf.setMeanAbsoluteError(eval.meanAbsoluteError());
        loadData.saveClassifierResult(rf);
        return ResponseEntity.ok(rf);
    }

    public ResponseEntity<ClassifierResult> applyIbk(ClassifierResult rf, String datasetName, Instances trainingData) throws Exception {

        IBk classifier = new IBk();
        try {
            if (trainingData.classIndex() == -1) {
                trainingData.setClassIndex(trainingData.numAttributes() - 1);
            }

        } catch (UnassignedClassException ex) {
            throw new Exception(ex.getMessage());
        }
        classifier.buildClassifier(trainingData);

        Evaluation eval = new Evaluation(trainingData);

        eval.crossValidateModel(classifier, trainingData, 10, new Random());

        rf.setNumInstances(eval.numInstances());
        rf.setCorrectlyClassified(eval.correct());
        rf.setMeanAbsoluteError(eval.meanAbsoluteError());
        loadData.saveClassifierResult(rf);
        return ResponseEntity.ok(rf);
    }


    public ResponseEntity<ClassifierResult> applyMlp(ClassifierResult rf, String datasetName, Instances trainingData) throws Exception {

        MultilayerPerceptron classifier = new MultilayerPerceptron();
        try {
            if (trainingData.classIndex() == -1) {
                trainingData.setClassIndex(trainingData.numAttributes() - 1);
            }

        } catch (UnassignedClassException ex) {
            throw new Exception(ex.getMessage());
        }
        classifier.buildClassifier(trainingData);

        Evaluation eval = new Evaluation(trainingData);

        eval.crossValidateModel(classifier, trainingData, 10, new Random());

        rf.setNumInstances(eval.numInstances());
        rf.setCorrectlyClassified(eval.correct());
        rf.setMeanAbsoluteError(eval.meanAbsoluteError());
        loadData.saveClassifierResult(rf);
        return ResponseEntity.ok(rf);
    }

    @Override
    public List<ClassifierResult> getResultsByUser(Authentication authentication) throws Exception {
        String username = authentication.getName();
        AppUser appUser = userRepo.findByUsername(username);
        List<ClassifierResult> ret = classifierRepo.findClassifierResultByPerformed_UserUploader(appUser);
        for (ClassifierResult r : ret
        ) {
            r.getPerformed().setUserUploader(null);
        }
        return ret;
    }

    @Override
    public List<ClassifierResult> getNewResultsByUser(Authentication authentication) throws Exception {
        String username = authentication.getName();
        AppUser appUser = userRepo.findByUsername(username);
        List<ClassifierResult> ret = classifierRepo.findClassifierResultByPerformed_UserUploaderAndSeenFalse(appUser);
        for (ClassifierResult r : ret
        ) {
            r.getPerformed().setUserUploader(null);
        }
        return ret;
    }

    @Override
    public List<FeatureResult> datasetsByUser(Authentication authentication) throws Exception {
        String username = authentication.getName();
        AppUser appUser = userRepo.findByUsername(username);
        List<FeatureResult> ret = featureRepo.findFeatureResultByPerformed_UserUploader(appUser);
        //make the user information not seen in frontend
        for (FeatureResult f : ret
        ) {
            Dataset data = f.getPerformed();
            data.setUserUploader(null);
            f.setPerformed(data);
        }
        return ret;
    }

    @Override
    public void setResultSeen(ClassifierResult classifierResult) throws Exception {
        ClassifierResult cr = classifierRepo.findClassifierResultById(classifierResult.getId());
        cr.setSeen(true);
        classifierRepo.save(cr);
    }
}
