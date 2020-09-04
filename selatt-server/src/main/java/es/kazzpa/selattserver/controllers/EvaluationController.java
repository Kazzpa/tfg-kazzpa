package es.kazzpa.selattserver.controllers;

import es.kazzpa.selattserver.models.ClassifierResult;
import es.kazzpa.selattserver.models.Dataset;
import es.kazzpa.selattserver.models.FeatureResult;
import es.kazzpa.selattserver.services.EvaluationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/evaluate")
public class EvaluationController {


    //TODO: WHEN LOADING FILTERED DATASETS:
    /*
    Remove removeFilter = new Remove();
    removeFilter.setAttributeIndicesArray(indices);
    removeFilter.setInputFormat(data);
    Instances newData = Filter.useFilter(data, removeFilter);
     */
    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @GetMapping(path = "naivebayes/{datasetName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClassifierResult> handleNaiveBayes(@PathVariable String datasetName) throws Exception {
        return evaluationService.handleNaiveBayes(datasetName);
    }

    @PostMapping(path = "naivebayes/filtered", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClassifierResult> handleNaiveBayesFiltered(@RequestBody FeatureResult dataset) throws Exception {
        return evaluationService.handleNaiveBayes(dataset);
    }

    @GetMapping(path = "bayesnet/{datasetName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClassifierResult> handleBayesNet(@PathVariable String datasetName) throws Exception {
        return evaluationService.handleBayesNet(datasetName);
    }

    @PostMapping(path = "bayesnet/filtered", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClassifierResult> handleBayesNetFiltered(@RequestBody FeatureResult dataset) throws Exception {
        return evaluationService.handleBayesNet(dataset);
    }

    @GetMapping(path = "hnb/{datasetName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClassifierResult> handleHiddenNaiveBayes(@PathVariable String datasetName) throws Exception {
        return evaluationService.handleHiddenNaiveBayes(datasetName);
    }

    @PostMapping(path = "hnb/filtered", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClassifierResult> handleHNBFiltered(@RequestBody FeatureResult dataset) throws Exception {
        return evaluationService.handleHiddenNaiveBayes(dataset);
    }

    @GetMapping(path = "ibk/{datasetName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClassifierResult> handleIbk(@PathVariable String datasetName) throws Exception {
        return evaluationService.handleIbk(datasetName);
    }

    @PostMapping(path = "ibk/filtered", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClassifierResult> handleIbkFiltered(@RequestBody FeatureResult dataset) throws Exception {
        return evaluationService.handleIbk(dataset);
    }

    @GetMapping(path = "mlp/{datasetName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClassifierResult> handleMultiLayerPerceptron(@PathVariable String datasetName) throws Exception {
        return evaluationService.handleMlp(datasetName);
    }

    @PostMapping(path = "mlp/filtered", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClassifierResult> handleMlpFiltered(@RequestBody FeatureResult dataset) throws Exception {
        return evaluationService.handleMlp(dataset);
    }

    @PostMapping(path = "result/seen")
    public void handleResultSeen(@RequestBody ClassifierResult classifierResult) throws Exception {
        evaluationService.setResultSeen(classifierResult);
    }

    @GetMapping("/results")
    public List<ClassifierResult> getResultsByUser(Authentication authentication) throws Exception {
        return evaluationService.getResultsByUser(authentication);
    }

    @GetMapping("/results/new")
    public List<ClassifierResult> getNewResultsByUser(Authentication authentication) throws Exception {
        return evaluationService.getNewResultsByUser(authentication);
    }

    @GetMapping("/datasets")
    public List<FeatureResult> getDatasetsByUser(Authentication authentication) throws Exception {
        return evaluationService.datasetsByUser(authentication);
    }
}
