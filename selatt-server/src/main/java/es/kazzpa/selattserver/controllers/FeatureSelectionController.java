package es.kazzpa.selattserver.controllers;

import es.kazzpa.selattserver.services.FeatureSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FeatureSelectionController {


    @Autowired
    private FeatureSelectionService featureSelectionService;

    //TODO USE GETMAPPING OR POSTMAPPING
    @RequestMapping(value = "/featureReduction/pca", method = {RequestMethod.GET})
    public String handleFeatureReduction() throws Exception {
        return featureSelectionService.handlePCAFeatures();
    }

    @RequestMapping(value = "/featureReduction/rp", method = {RequestMethod.GET})
    public String handleRP() throws Exception {
        return featureSelectionService.handleRandomizedProjectionFeatures();
    }


    @RequestMapping(value = "/featureReduction/cfs", method = {RequestMethod.GET})
    public String handleCfsSubsetEval() throws Exception {
        return featureSelectionService.handleCFSSubsetEval();
    }

    @RequestMapping(value = "/featureReduction/rp/plot", method = {RequestMethod.GET})
    public void handlePlotRp() throws Exception {
        featureSelectionService.plotRP();
    }

    @RequestMapping(value = "/featureReduction/pca/plot", method = {RequestMethod.GET})
    public void handlePlotPCA() throws Exception {
        featureSelectionService.plotPCA();
    }

}
