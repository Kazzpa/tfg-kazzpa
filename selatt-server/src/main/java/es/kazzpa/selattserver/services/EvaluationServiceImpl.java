package es.kazzpa.selattserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

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
    public String handleNaiveBayes(String fileName) throws Exception {
        Instances trainingData = loadData.getInstancesFromAnyFile(fileName);
        return applyNaiveBayes(fileName, trainingData);
    }


    public String applyNaiveBayes(String filename, Instances trainingData) throws Exception {
        //try {
        NaiveBayes classifier = new NaiveBayes();
        trainingData.setClassIndex(trainingData.classIndex());
        System.out.println(trainingData.classIndex());
        classifier.buildClassifier(trainingData);

        Evaluation eval = new Evaluation(trainingData);
        eval.evaluateModel(classifier, trainingData);
        System.out.println(eval.numInstances());
        return eval.toSummaryString();


    }
}
