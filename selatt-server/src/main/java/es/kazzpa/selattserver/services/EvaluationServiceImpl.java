package es.kazzpa.selattserver.services;

import es.kazzpa.selattserver.models.Algorithm;
import es.kazzpa.selattserver.models.Dataset;
import es.kazzpa.selattserver.models.ResultFilter;
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
    public ResponseEntity<ResultFilter> handleNaiveBayes(String datasetName) throws Exception {
        Instances trainingData = loadData.getInstancesFromAnyFile(datasetName);
        return applyNaiveBayes(datasetName, trainingData);
    }


    public ResponseEntity<ResultFilter> applyNaiveBayes(String datasetName, Instances trainingData) throws Exception {
        Algorithm naiveBayes = loadData.getAlgorithm("Naive Bayes", "Weka Package");
        Dataset dataset = loadData.getDataset(datasetName);
        ResultFilter rf = loadData.checkIfAlreadyExists(naiveBayes, dataset);
        if (rf.getJsonAttributes() != null) {
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
        System.out.println(eval.numInstances());
        System.out.println(eval.avgCost());
        System.out.println(eval.correct());
        //System.out.println(eval.fMeasure(trainingData.classIndex()-1));
        System.out.println(eval.incorrect());
        //System.out.println(eval.correlationCoefficient());
        System.out.println(eval.errorRate());
        System.out.println(eval.meanAbsoluteError());
        System.out.println(eval.totalCost());
        System.out.println(eval.unclassified());
        System.out.println(eval.weightedPrecision());
        loadData.saveResultFilter(summary, rf, naiveBayes, dataset);
        return ResponseEntity.ok(rf);


    }
}
