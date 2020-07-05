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
    private final FileStorageService fileStorageService;
    @Autowired
    private final LoadData loadData;

    public EvaluationServiceImpl(FileStorageService fileStorageService, LoadData loadData) {
        this.fileStorageService = fileStorageService;
        this.loadData = loadData;
    }

    @Override
    public String handleNaiveBayes(String fileName) throws Exception {
        Instances trainingData = loadData.getInstancesFromAnyFile(fileName);
        return applyNaiveBayes(fileName, trainingData);
    }


    public String applyNaiveBayes(String filename, Instances trainingData) throws Exception {
        try {
            Classifier classifier = new NaiveBayes();

            classifier.buildClassifier(trainingData);

            Evaluation eval = new Evaluation(trainingData);
            eval.evaluateModel(classifier, trainingData);
            return eval.toSummaryString();
        } catch (Exception ex) {
            throw new Exception("Archivo no pudo ser  cargado como resource");
        }


    }
}
