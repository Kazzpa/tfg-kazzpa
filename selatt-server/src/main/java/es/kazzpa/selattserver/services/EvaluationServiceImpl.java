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
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.UnassignedClassException;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

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
    public ResponseEntity<ClassifierResult> handleNaiveBayes(String datasetName) throws Exception {
        Instances trainingData = loadData.getInstancesFromAnyFile(datasetName);
        Algorithm naiveBayes = loadData.getAlgorithm("Naive Bayes", "Weka Package");
        Dataset dataset2 = loadData.getDataset(datasetName);
        ClassifierResult rf = loadData.checkIfClassifierResultAlreadyExists(naiveBayes, dataset2, null);
        if (rf.getFinishedDate() != null) {
            return ResponseEntity.ok(rf);
        }
        return applyNaiveBayes(rf, datasetName, trainingData);
    }

    @Override
    public ResponseEntity<ClassifierResult> handleNaiveBayes(FeatureResult dataset) throws Exception {
        String datasetName = dataset.getPerformed().getFilename();
        String attributesSelected = dataset.getAttributesSelected();
        Instances trainingData = loadData.getInstancesFromAnyFile(datasetName);

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
        Instances newData = Filter.useFilter(trainingData, removeFilter);
        Algorithm naiveBayes = loadData.getAlgorithm("Naive Bayes", "Weka Package");
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

        eval.crossValidateModel(classifier, trainingData,10,new Random());

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
        classifierResult.setSeen(true);
        classifierRepo.save(classifierResult);
    }
}
