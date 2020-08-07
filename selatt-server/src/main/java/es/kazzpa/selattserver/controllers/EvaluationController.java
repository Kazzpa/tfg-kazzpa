package es.kazzpa.selattserver.controllers;

import es.kazzpa.selattserver.models.ResultFilter;
import es.kazzpa.selattserver.services.EvaluationService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/evaluate")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @GetMapping(path = "naivebayes/{datasetName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultFilter> handleNaiveBayesEvaluation(@PathVariable String datasetName) throws Exception {
        return evaluationService.handleNaiveBayes(datasetName);
    }
}
