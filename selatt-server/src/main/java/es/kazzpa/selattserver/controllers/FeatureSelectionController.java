package es.kazzpa.selattserver.controllers;

import es.kazzpa.selattserver.models.Dataset;
import es.kazzpa.selattserver.models.FeatureResult;
import es.kazzpa.selattserver.services.FeatureSelectionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/featureSelection")
public class FeatureSelectionController {


    private final FeatureSelectionService featureSelectionService;

    public FeatureSelectionController(FeatureSelectionService featureSelectionService) {
        this.featureSelectionService = featureSelectionService;
    }

    @GetMapping(path = "scs/{datasetName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FeatureResult> handleScatterSearch(@PathVariable String datasetName) throws Exception {
        return featureSelectionService.handleScatterSearch(datasetName);
    }

    @GetMapping(path = "fcbf/{datasetName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FeatureResult> handleFCBF(@PathVariable String datasetName) throws Exception {
        return featureSelectionService.handleFCBF(datasetName);
    }

    @GetMapping(path = "ranker/{datasetName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FeatureResult> handleRanker(@PathVariable String datasetName) throws Exception {
        return featureSelectionService.handleRanker(datasetName);
    }

    @GetMapping(path = "bestfirst/{datasetName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FeatureResult> handleBestFirst(@PathVariable String datasetName) throws Exception {
        return featureSelectionService.handleBestFirst(datasetName);
    }

    @GetMapping(path = "exhaustive/{datasetName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FeatureResult> handleExhaustive(@PathVariable String datasetName) throws Exception {
        return featureSelectionService.handleExhaustive(datasetName);
    }

    @GetMapping(path = "vns/{datasetName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FeatureResult> handleVNS(@PathVariable String datasetName) throws Exception {
        return featureSelectionService.handleVNS(datasetName);
    }

    @GetMapping("results")
    public List<FeatureResult> getResultsByUser(Authentication authentication) throws Exception {
        return featureSelectionService.getResultsByUser(authentication);
    }

    @GetMapping("results/new")
    public List<FeatureResult> getNewResultsByUser(Authentication authentication) throws Exception {
        return featureSelectionService.getNewResultsByUser(authentication);
    }

    @GetMapping("/datasets")
    public List<Dataset> getDatasetsByUser(Authentication authentication) throws Exception {
        return featureSelectionService.datasetsByUser(authentication);
    }

    @PostMapping(path = "result/seen")
    public void handleResultSeen(@RequestBody FeatureResult featureResult) throws Exception {
        featureSelectionService.setResultSeen(featureResult);
    }

    @Bean
    @Profile("test")
    CommandLineRunner clr(FeatureSelectionService featureSelectionService) {
        return args -> {
            featureSelectionService.handleLoadDefaultData();
        };
    }
}
