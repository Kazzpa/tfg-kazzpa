package es.kazzpa.selattserver.services;


import es.kazzpa.selattserver.models.*;
import es.kazzpa.selattserver.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


import upo.jcu.math.set.Subset;
import weka.attributeSelection.*;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.Tag;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.RandomProjection;
import weka.filters.unsupervised.attribute.Remove;

import upo.jcu.math.data.dataset.DataType;
import upo.jml.data.dataset.ClassificationDataset;
import upo.jml.data.dataset.DatasetUtils;
import upo.jml.prediction.classification.fss.algorithms.FSPredGroupsBasicVNS;
import upo.jml.prediction.classification.fss.core.FSObjectiveFunction;
import upo.jml.prediction.classification.fss.core.FSSolution;
import upo.jml.prediction.classification.fss.evaluators.CfsEvaluator;

import java.io.FileWriter;
import java.util.*;

@Service("featureSelectionService")
public class FeatureSelectionServiceImpl implements FeatureSelectionService {

    private final FileFactory fileFactory;
    @Autowired
    private final FeatureResultRepository featureRepo;
    @Autowired
    private final DatasetRepository dataRepo;
    @Autowired
    private final AlgorithmRepository algoRepo;
    @Autowired
    private final AppUserRepository userRepo;
    private final LoadData loadData;
    /** search direction: backward */
    protected static final int SELECTION_BACKWARD = 0;
    /** search direction: forward */
    protected static final int SELECTION_FORWARD = 1;
    /** search direction: bidirectional */
    protected static final int SELECTION_BIDIRECTIONAL = 2;
    /** search directions */
    public static final Tag[] TAGS_SELECTION = {
            new Tag(SELECTION_BACKWARD, "Backward"),
            new Tag(SELECTION_FORWARD, "Forward"),
            new Tag(SELECTION_BIDIRECTIONAL, "Bi-directional"), };

    public FeatureSelectionServiceImpl(FileFactory fileFactory, DatasetRepository dataRepo, LoadData loadData, AlgorithmRepository algoRepo, AppUserRepository userRepo, FeatureResultRepository resRepo) {
        this.fileFactory = fileFactory;
        this.featureRepo = resRepo;
        this.dataRepo = dataRepo;
        this.loadData = loadData;
        this.algoRepo = algoRepo;
        this.userRepo = userRepo;
    }

    public void handleLoadDefaultData() {
        loadData.loadDefaultDataBase();
    }

    public ResponseEntity<FeatureResult> handleVNS(String datasetName) throws Exception {

        ClassificationDataset dataset = loadData.getClassDatasetFromArff(datasetName);
        if (!dataset.getDataType().equals(DataType.CATEGORICAL)) {
            dataset = DatasetUtils.dicretizeViaFayyad(dataset);
        }

        return applyVNS(datasetName, dataset);
    }

    public ResponseEntity<FeatureResult> handleFCBF(String datasetName) throws Exception {
        Instances trainingData = loadData.getInstancesFromAnyFile(datasetName);
        if (trainingData == null) {
            return ResponseEntity.notFound().build();
        }
        return applyFCBF(datasetName, trainingData);
    }

    public ResponseEntity<FeatureResult> handleScatterSearch(String datasetName) throws Exception {
        Instances trainingData = loadData.getInstancesFromAnyFile(datasetName);
        if (trainingData == null) {
            return ResponseEntity.notFound().build();
        }
        return applyScatterSearch(datasetName, trainingData);
    }

    public ResponseEntity<FeatureResult> handleRanker(String datasetName) throws Exception {
        Instances trainingData = loadData.getInstancesFromAnyFile(datasetName);
        if (trainingData == null) {
            return ResponseEntity.notFound().build();
        }
        return applyRanker(datasetName, trainingData);
    }

    public ResponseEntity<FeatureResult> handleBestFirst(String datasetName) throws Exception {
        Instances trainingData = loadData.getInstancesFromAnyFile(datasetName);
        if (trainingData == null) {
            return ResponseEntity.notFound().build();
        }
        return applyBestFirst(datasetName, trainingData);
    }
    public ResponseEntity<FeatureResult> handleExhaustive(String datasetName) throws Exception {
        Instances trainingData = loadData.getInstancesFromAnyFile(datasetName);
        if (trainingData == null) {
            return ResponseEntity.notFound().build();
        }
        return applyExhaustive(datasetName, trainingData);
    }

    @Override
    public List<FeatureResult> getResultsByUser(Authentication authentication) throws Exception {
        String username = authentication.getName();
        AppUser appUser = userRepo.findByUsername(username);
        List<FeatureResult> ret = featureRepo.findFeatureResultByPerformed_UserUploader(appUser);
        for (FeatureResult r : ret
        ) {
            r.getPerformed().setUserUploader(null);
        }
        return ret;
    }

    @Override
    public List<FeatureResult> getNewResultsByUser(Authentication authentication) throws Exception {
        String username = authentication.getName();
        AppUser appUser = userRepo.findByUsername(username);
        List<FeatureResult> ret = featureRepo.findFeatureResultByPerformed_UserUploaderAndSeenFalse(appUser);
        for (FeatureResult r : ret
        ) {
            r.getPerformed().setUserUploader(null);
        }
        return ret;
    }

    public void setResultSeen(FeatureResult featureResult) throws Exception {
        FeatureResult fr = featureRepo.findFeatureResultById(featureResult.getId());
        fr.setSeen(true);
        featureRepo.save(fr);
    }

    @Override
    public List<Dataset> datasetsByUser(Authentication authentication) throws Exception {
        String username = authentication.getName();
        AppUser appUser = userRepo.findByUsername(username);
        List<Dataset> ret = dataRepo.findDatasetByUserUploader(appUser);
        //make the user information not seen in frontend
        for (Dataset d : ret
        ) {
            d.setUserUploader(null);
        }
        return ret;
    }


    public ResponseEntity<FeatureResult> applyFCBF(String datasetName, Instances trainingData) throws Exception {

        //Check if the resultfilter has been processed before
        Algorithm fcbf = loadData.getAlgorithm("FastCorrelationBasedFilter", "Weka package","Feature Selection");
        Dataset dataset = loadData.getDataset(datasetName);
        FeatureResult rf = loadData.checkIfFeatureAlreadyExists(fcbf, dataset);
        if (rf.getFinishedDate() != null) {
            return ResponseEntity.ok(rf);
        }
        //fcbf heuristico : no generico, cerrado la estrategia de busqueda y evaluacion
        //utiliza: simuncertAttributeSetEval
        SymmetricalUncertAttributeSetEval eval = new SymmetricalUncertAttributeSetEval();
        //default class index to last att if undefined
        //https://weka.sourceforge.io/doc.dev/weka/core/Instances.html
        if (trainingData.classIndex() < 0) {
            trainingData.setClassIndex(trainingData.numAttributes() - 1);
        }
        eval.buildEvaluator(trainingData);
        FCBFSearch fcbfSearch = new FCBFSearch();
        int[] sol = fcbfSearch.search(eval, trainingData);
        rf = loadData.saveFeatureSelectionResult(rf, sol);
        return ResponseEntity.ok(rf);

    }

    public ResponseEntity<FeatureResult> applyScatterSearch(String datasetName, Instances trainingData) throws Exception {

        Algorithm scatterSearch = loadData.getAlgorithm("ScatterSearchV1", "Weka package","Feature Selection");
        Dataset dataset = loadData.getDataset(datasetName);
        FeatureResult fr = loadData.checkIfFeatureAlreadyExists(scatterSearch, dataset);

        if (fr.getFinishedDate() != null) {
            return ResponseEntity.ok(fr);
        }

        CfsSubsetEval eval = new CfsSubsetEval();
        //default class index to last att if undefined
        //https://weka.sourceforge.io/doc.dev/weka/core/Instances.html
        if (trainingData.classIndex() < 0) {
            trainingData.setClassIndex(trainingData.numAttributes() - 1);
        }
        eval.buildEvaluator(trainingData);

        ScatterSearchV1 scatterSearchV1 = new ScatterSearchV1();
        int[] sol = scatterSearchV1.search(eval, trainingData);
        fr = loadData.saveFeatureSelectionResult(fr, sol);
        return ResponseEntity.ok(fr);
    }

    public ResponseEntity<FeatureResult> applyRanker(String datasetName, Instances trainingData) throws Exception {

        //Check if the resultfilter has been processed before
        Algorithm ranker = loadData.getAlgorithm("Ranker", "Weka package","Feature Selection");
        Dataset dataset = loadData.getDataset(datasetName);
        FeatureResult fr = loadData.checkIfFeatureAlreadyExists(ranker, dataset);
        if (fr.getFinishedDate() != null) {
            return ResponseEntity.ok(fr);
        }
        //fcbf heuristico : no generico, cerrado la estrategia de busqueda y evaluacion
        //utiliza: simuncertAttributeSetEval
        CorrelationAttributeEval eval = new CorrelationAttributeEval();
        //default class index to last att if undefined
        //https://weka.sourceforge.io/doc.dev/weka/core/Instances.html
        if (trainingData.classIndex() < 0) {
            trainingData.setClassIndex(trainingData.numAttributes() - 1);
        }
        eval.buildEvaluator(trainingData);
        Ranker rankerAlg = new Ranker();
        rankerAlg.setThreshold(0.20);
        int[] sol = rankerAlg.search(eval, trainingData);
        String aux = "[";
        double[][] rankedAttr = rankerAlg.rankedAttributes();
        for (double[] row : rankedAttr) {
            aux += "\n(";
            for (double elem : row) {
                aux += "," + elem;
            }
            aux += ")";
        }
        aux += "]";
        System.out.println(aux);
        fr = loadData.saveFeatureSelectionResult(fr, sol);
        return ResponseEntity.ok(fr);

    }

    public ResponseEntity<FeatureResult> applyBestFirst(String datasetName, Instances trainingData) throws Exception {

        //TODO: FIX WORKING IT GIVES FULL ATTRIBUTES AS SELECTED
        //Check if the resultfilter has been processed before
        Algorithm bestFirst = loadData.getAlgorithm("Best First", "Weka package","Feature Selection");
        Dataset dataset = loadData.getDataset(datasetName);
        FeatureResult fr = loadData.checkIfFeatureAlreadyExists(bestFirst, dataset);
        if (fr.getFinishedDate() != null) {
            return ResponseEntity.ok(fr);
        }
        WrapperSubsetEval eval = new WrapperSubsetEval();
        //default class index to last att if undefined
        //https://weka.sourceforge.io/doc.dev/weka/core/Instances.html
        if (trainingData.classIndex() < 0) {
            trainingData.setClassIndex(trainingData.numAttributes() - 1);
        }
        eval.buildEvaluator(trainingData);
        BestFirst bestFirstSearch = new BestFirst();
        bestFirstSearch.setStartSet("1-" + trainingData.numAttributes());
        bestFirstSearch.setSearchTermination(trainingData.numAttributes()/2);
        bestFirstSearch.setDirection(new SelectedTag(SELECTION_BACKWARD,TAGS_SELECTION));
        int[] sol = bestFirstSearch.search(eval, trainingData);

        fr = loadData.saveFeatureSelectionResult(fr, sol);
        return ResponseEntity.ok(fr);

    }
    public ResponseEntity<FeatureResult> applyExhaustive(String datasetName, Instances trainingData) throws Exception {

        //Check if the resultfilter has been processed before
        Algorithm exhaustive = loadData.getAlgorithm("Exhaustive Search", "Weka package","Feature Selection");
        Dataset dataset = loadData.getDataset(datasetName);
        FeatureResult fr = loadData.checkIfFeatureAlreadyExists(exhaustive, dataset);
        if (fr.getFinishedDate() != null) {
            return ResponseEntity.ok(fr);
        }
        WrapperSubsetEval eval = new WrapperSubsetEval();
        //default class index to last att if undefined
        //https://weka.sourceforge.io/doc.dev/weka/core/Instances.html
        if (trainingData.classIndex() < 0) {
            trainingData.setClassIndex(trainingData.numAttributes() - 1);
        }
        eval.buildEvaluator(trainingData);
        BestFirst bestFirstSearch = new BestFirst();
        bestFirstSearch.setStartSet("1-" + (trainingData.numAttributes()-1));
        bestFirstSearch.setSearchTermination(trainingData.numAttributes()/2);
        bestFirstSearch.setDirection(new SelectedTag(SELECTION_BACKWARD,TAGS_SELECTION));
        int[] sol = bestFirstSearch.search(eval, trainingData);

        fr = loadData.saveFeatureSelectionResult(fr, sol);
        return ResponseEntity.ok(fr);

    }

    public ResponseEntity<FeatureResult> applyVNS(String datasetName, ClassificationDataset dataset) throws Exception {

        Algorithm vns = loadData.getAlgorithm("VariableNeighbourhoodSearch", "Paper published","Feature Selection");
        Dataset dataset1 = loadData.getDataset(datasetName);
        FeatureResult rf = loadData.checkIfFeatureAlreadyExists(vns, dataset1);
        if (rf.getFinishedDate() != null) {
            Dataset aux = rf.getPerformed();
            aux.setUserUploader(null);
            rf.setPerformed(aux);
            return ResponseEntity.ok(rf);
        }
        FSObjectiveFunction of = new CfsEvaluator(dataset.getCategoricalData(), dataset.getLabels());
        of.buildEvaluator();
        FSPredGroupsBasicVNS bvns = new FSPredGroupsBasicVNS(dataset.getCategoricalData(), dataset.getLabels(), of, true);
        FSSolution solution = bvns.search();
        Subset<Integer> sol = solution.getSubset();
        Iterator it = sol.elements();
        List<Integer> lista = new ArrayList<Integer>();

        while (it.hasNext()) {
            int next = (int) it.next();
            lista.add(next);
        }
        int[] solArr = new int[lista.size()];
        for (int i = 0; i < solArr.length; i++) {
            solArr[i] = lista.get(i);
        }

        rf = loadData.saveFeatureSelectionResult(rf, solArr);
        return ResponseEntity.ok(rf);
    }

}