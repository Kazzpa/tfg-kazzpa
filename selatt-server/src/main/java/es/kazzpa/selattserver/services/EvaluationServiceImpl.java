package es.kazzpa.selattserver.services;

import es.kazzpa.selattserver.models.Algorithm;
import es.kazzpa.selattserver.models.Dataset;
import es.kazzpa.selattserver.models.ClassifierResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.UnassignedClassException;

@Service
public class EvaluationServiceImpl implements EvaluationService {

    @Autowired
    private final FeatureSelectionService featureSelectionService;
    @Autowired
    private final LoadData loadData;

    public EvaluationServiceImpl(FeatureSelectionService featureSelectionService, LoadData loadData) {
        this.featureSelectionService = featureSelectionService;
        this.loadData = loadData;
    }

    @Override
    public ResponseEntity<ClassifierResult> handleNaiveBayes(String datasetName) throws Exception {
        Instances trainingData = loadData.getInstancesFromAnyFile(datasetName);
        return applyNaiveBayes(datasetName, trainingData);
    }


    public ResponseEntity<ClassifierResult> applyNaiveBayes(String datasetName, Instances trainingData) throws Exception {
        Algorithm naiveBayes = loadData.getAlgorithm("Naive Bayes", "Weka Package");
        Dataset dataset = loadData.getDataset(datasetName);
        ClassifierResult rf = loadData.checkIfClassifierAlreadyExists(naiveBayes, dataset);
        if (rf.getFinishedDate() != null) {
            return ResponseEntity.ok(rf);
        }
        NaiveBayes classifier = new NaiveBayes();
        try {
            if (trainingData.classIndex() == -1) {
                trainingData.setClassIndex(trainingData.numAttributes() - 1);
            }

        } catch (UnassignedClassException ex) {
            System.out.println(trainingData.classIndex());
            System.out.println(trainingData.numClasses());
            throw new Exception(ex.getMessage());
        }
        System.out.println(trainingData.classIndex());
        classifier.buildClassifier(trainingData);

        Evaluation eval = new Evaluation(trainingData);
        eval.evaluateModel(classifier, trainingData);

        String summary = eval.toSummaryString();

        rf.setNumInstances(eval.numInstances());
        rf.setCorrectlyClassified(eval.correct());
        rf.setMeanAbsoluteError(eval.meanAbsoluteError());
        loadData.saveClassifierResult(rf);
        return ResponseEntity.ok(rf);


    }
}
