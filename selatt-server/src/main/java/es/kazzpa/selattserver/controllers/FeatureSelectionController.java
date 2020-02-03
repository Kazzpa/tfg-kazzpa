package es.kazzpa.selattserver.controllers;

import es.kazzpa.selattserver.models.ResultFilter;
import es.kazzpa.selattserver.services.FeatureSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Result;

@RestController
@RequestMapping(path = "/featureReduction")
public class FeatureSelectionController {


    @Autowired
    private FeatureSelectionService featureSelectionService;

    //TODO USE JSON
    @GetMapping(path = "/pca", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResultFilter handleFeatureReduction() throws Exception {
        return featureSelectionService.handlePCAFeatures();
    }

    @GetMapping(path = "/rp", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String handleRP() throws Exception {
        return featureSelectionService.handleRandomizedProjectionFeatures();
    }


    @GetMapping(path = "/cfs", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String handleCfsSubsetEval() throws Exception {
        return featureSelectionService.handleCFSSubsetEval();
    }

    @GetMapping(path = "/rp/plot", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void handlePlotRp() throws Exception {
        featureSelectionService.plotRP();
    }

    @GetMapping(path = "/pca/plot", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void handlePlotPCA() throws Exception {
        featureSelectionService.plotPCA();
    }

}
