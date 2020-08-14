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


    //TODO: quitar / al principio y crear metodo que cargue dataset de DB.
    @GetMapping(path = "pca/{datasetName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public FeatureResult handleFeatureReduction(@PathVariable String datasetName) throws Exception {
        return featureSelectionService.handlePCAFeatures();
    }

    @GetMapping(path = "Scs/{datasetName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FeatureResult> handleScatterSearch(@PathVariable String datasetName) throws Exception {
        return featureSelectionService.handleScatterSearch(datasetName);
    }

    @GetMapping(path = "fcbf/{datasetName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FeatureResult> handleFCBF(@PathVariable String datasetName) throws Exception {
        return featureSelectionService.handleFCBF(datasetName);
    }

    @GetMapping(path = "vns/{datasetName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FeatureResult> handleVNS(@PathVariable String datasetName) throws Exception {
        return featureSelectionService.handleVNS(datasetName);
    }

    @GetMapping(path = "pca", produces = MediaType.APPLICATION_JSON_VALUE)
    public FeatureResult handleFeatureReduction() throws Exception {
        return featureSelectionService.handlePCAFeatures();
    }

    @GetMapping(path = "/rp", produces = MediaType.APPLICATION_JSON_VALUE)
    public String handleRP() throws Exception {
        return featureSelectionService.handleRandomizedProjectionFeatures();
    }


    @GetMapping(path = "cfs", produces = MediaType.APPLICATION_JSON_VALUE)
    public String handleCfsSubsetEval() throws Exception {
        return featureSelectionService.handleCFSSubsetEval();
    }

    @GetMapping(path = "rp/plot", produces = MediaType.APPLICATION_JSON_VALUE)
    public void handlePlotRp() throws Exception {
        featureSelectionService.plotRP();
    }

    @GetMapping(path = "pca/plot", produces = MediaType.APPLICATION_JSON_VALUE)
    public FeatureResult handlePlotPCA() throws Exception {
        return featureSelectionService.plotPCA();
    }

    @GetMapping("/resultsByUser")
    public List<FeatureResult> getResultsByUser(Authentication authentication) throws Exception {
        return featureSelectionService.resultsByUser(authentication);
    }


    @GetMapping("/filesByUser")
    public List<Dataset> getDatasetsByUser(Authentication authentication) throws Exception {
        return featureSelectionService.datasetsByUser(authentication);
    }

    @Bean
    @Profile("test")
    CommandLineRunner clr(FeatureSelectionService featureSelectionService) {
        return args -> {
            featureSelectionService.handleLoadDefaultData();
        };
    }
}
