package es.kazzpa.selattserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;
import weka.core.converters.JSONLoader;

@Service
public class EvaluationServiceImpl implements EvaluationService {

    @Autowired
    private final FileStorageService fileStorageService;

    public EvaluationServiceImpl(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    public String naiveBayesProcess(String filename) throws Exception {
        Resource resource;
        try {
            resource = fileStorageService.loadFileAsResource(filename);
            JSONLoader loader = new JSONLoader();
            loader.setSource(resource.getFile());
            Instances training = loader.getDataSet();
            Classifier classifier = new NaiveBayes();

            classifier.buildClassifier(training);

            Evaluation eval = new Evaluation(training);
            eval.evaluateModel(classifier, training);
            return eval.toSummaryString();
        } catch (Exception ex) {
            throw new Exception("Archivo no pudo ser  cargado como resource");
        }


    }
}
