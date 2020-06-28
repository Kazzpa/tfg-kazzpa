package es.kazzpa.selattserver.controllers;

import es.kazzpa.selattserver.models.ResultFilter;
import es.kazzpa.selattserver.models.UploadFileResponse;
import es.kazzpa.selattserver.services.FeatureSelectionService;
import es.kazzpa.selattserver.services.FileStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/featureReduction")
public class FeatureSelectionController {


    private final FeatureSelectionService featureSelectionService;

    public FeatureSelectionController(FeatureSelectionService featureSelectionService) {
        this.featureSelectionService = featureSelectionService;
    }


    //TODO: quitar / al principio y crear metodo que cargue dataset de DB.
    @GetMapping(path = "pca/{datasetName}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResultFilter handleFeatureReduction(@PathVariable String datasetName) throws Exception {
        return featureSelectionService.handlePCAFeatures();
    }

    @GetMapping(path = "pca", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResultFilter handleFeatureReduction() throws Exception {
        return featureSelectionService.handlePCAFeatures();
    }

    @GetMapping(path = "/rp", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String handleRP() throws Exception {
        return featureSelectionService.handleRandomizedProjectionFeatures();
    }


    @GetMapping(path = "cfs", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String handleCfsSubsetEval() throws Exception {
        return featureSelectionService.handleCFSSubsetEval();
    }

    @GetMapping(path = "rp/plot", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void handlePlotRp() throws Exception {
        featureSelectionService.plotRP();
    }

    @GetMapping(path = "pca/plot", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResultFilter handlePlotPCA() throws Exception {
        return featureSelectionService.plotPCA();
    }


    @Bean
    @Profile("test")
    CommandLineRunner clr(FeatureSelectionService featureSelectionService) {
        return args -> {
            featureSelectionService.handleLoadDefaultData();
        };
    }
}
