package es.kazzpa.selattserver.controllers;

import es.kazzpa.selattserver.models.ResultFilter;
import es.kazzpa.selattserver.services.EvaluationService;
import es.kazzpa.selattserver.services.FeatureSelectionService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/evaluate")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @GetMapping(path = "naivebayes", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String handleNaiveBayesEvaluation(@PathVariable String filename) throws Exception {
        System.out.println(filename);
        return evaluationService.naiveBayesProcess(filename);
    }
}
