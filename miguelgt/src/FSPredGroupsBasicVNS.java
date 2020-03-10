/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
/**
 * FSSimpleBasicVNS.java Copyright (C) 2008-2009 Miguel Garcia Torres
 */
package upo.jml.prediction.classification.fss.algorithms;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
//import java.util.logging.Logger;
import upo.jcu.math.data.dataset.DataType;
import upo.jcu.math.opt.mth.vnsearch.BasicVariableNeighborhoodSearch;
import upo.jcu.utils.ArrayUtils;
import upo.jml.data.dataset.ClassificationDataset;
import upo.jml.data.dataset.DatasetUtils;
import upo.jml.data.dataset.ToyDatasets;
import upo.jml.prediction.classification.classifiers.BayesClassifier;
import upo.jml.prediction.classification.fss.core.FSObjectiveFunction;
import upo.jml.prediction.classification.fss.core.FSSolution;
import upo.jml.prediction.classification.fss.core.FeatureBags;
import upo.jml.prediction.classification.fss.core.SOFFSSearchAlgorithm;
import upo.jml.prediction.classification.fss.evaluators.CfsEvaluator;
import upo.jml.prediction.classification.fss.evaluators.WrapperEvaluator;

/**
 * @see Garcia-Torres et al. Information Sciences, 2015.
 * @author mgarciat[at]upo[dot]es
 */
public class FSPredGroupsBasicVNS extends BasicVariableNeighborhoodSearch<Integer, FSSolution, FSObjectiveFunction> implements SOFFSSearchAlgorithm {

//    static final Logger logger = Logger.getLogger(FSPredGroupsBasicVNS.class.getName());
    protected int numFeatures = -1;
    protected Random random = null;
    protected long seed = 12323;
    protected int[][] data;
    protected int[][] values;
    protected int[] labels;
    protected FeatureBags featureSpaceSearch;
    protected boolean overlap = true;
    private boolean isKmaxPct = true;
    /**
     * 
     * @param idata
     * @param vals
     * @param ilabels
     * @param of
     * @throws Exception 
     */
    public FSPredGroupsBasicVNS(int[][] idata, int[][] vals, int[] ilabels, FSObjectiveFunction of) throws Exception {
        this.numFeatures = idata[0].length;
        this.data = idata;
        this.labels = ilabels;
        this.values = vals;
        super.setObjectiveFunction(of);
        this.random = new Random(this.seed);
        this.overlap = true;
    }

    public FSPredGroupsBasicVNS(int[][] idata, int[] ilabels, FSObjectiveFunction of) throws Exception {
        this.numFeatures = idata[0].length;
        this.data = idata;
        this.labels = ilabels;
        super.setObjectiveFunction(of);
        this.random = new Random(this.seed);
        this.overlap = true;
    }

    public FSPredGroupsBasicVNS(int[][] idata, int[] ilabels, FSObjectiveFunction of, boolean overl) throws Exception {
        this.numFeatures = idata[0].length;
        this.data = idata;
        this.labels = ilabels;
        super.setObjectiveFunction(of);
        this.random = new Random(this.seed);
        this.overlap = overl;
    }

    public FSPredGroupsBasicVNS() {}
    /**
     * Constructor
     *
     * @param idata
     * @param ilabels
     * @throws Exception
     */
    public FSPredGroupsBasicVNS(int[][] idata, int[] ilabels) throws Exception {
        this.numFeatures = idata[0].length;
        this.data = idata;
        this.labels = ilabels;
        this.random = new Random(this.seed);
        this.overlap = true;
    }

    public FSPredGroupsBasicVNS(int[][] idata, int[][] vals, int[] ilabels) throws Exception {
        this.numFeatures = idata[0].length;
        this.data = idata;
        this.values = vals;
        this.labels = ilabels;
        this.random = new Random(this.seed);
        this.overlap = true;
    }
    
    @Override
    public void setDataset(ClassificationDataset dataset) throws Exception {
        this.data = dataset.getCategoricalData();
        this.labels = dataset.getLabels();
        this.numFeatures = dataset.numberOfFeatures();
        this.values = dataset.getCategoricalValuesIndexes();
    }
    
    public boolean isOverlap() {
        return overlap;
    }

    public void setOverlap(boolean overlap) {
        this.overlap = overlap;
    }
    
    public void setSeed(long seed) {
        this.seed = seed;
        this.random = new Random(this.seed);
    }

    public boolean isIsKmaxPct() {
        return isKmaxPct;
    }

    public void setIsKmaxPct(boolean isKmaxPct) {
        this.isKmaxPct = isKmaxPct;
    }

    public void setNumFeatures(int numFeatures) {
        this.numFeatures = numFeatures;
    }
    
    @Override
    public FSSolution generateInitialSolution() throws Exception {
        FSSolution solution = null;
        if (!this.overlap) {
            FCBFBagSearch fcbf = null;
            if (this.values == null) {
                fcbf = new FCBFBagSearch(this.data, this.labels);
            } else {
                fcbf = new FCBFBagSearch(this.data, this.values, this.labels, 0.);
            }
            solution = fcbf.search();

            this.featureSpaceSearch = new FeatureBags(fcbf.getBags());
        } else {
            FCBFOverlappingBagSearch fcbf = null;
            if (this.values == null) {
                fcbf = new FCBFOverlappingBagSearch(this.data, this.labels);
            } else {
                //System.out.println(ArrayUtils.toString(this.values));
                //for (int i = 0; i < this.values.length; i++) {
                //    if (this.values[i].length > 1) {
                //        System.out.println("[" + i + "]: " + this.values[i].length);
                //    }
                //}
                fcbf = new FCBFOverlappingBagSearch(this.data, this.values, this.labels, 0.);
            }
            solution = fcbf.search();
            this.featureSpaceSearch = new FeatureBags(fcbf.getBags());
        }
        //logger.info(this.featureSpaceSearch.toString());
        super.objectiveFunction.evaluate(solution);
        //logger.info(solution.toString());
        //System.out.println("kmax: " + super.kMax);
        //System.out.println("#features: " + this.numFeatures);
        //System.out.println("is pct: " + this.isKmaxPct);
        if (this.isKmaxPct) {
            super.kMax = (int) Math.floor(((double)super.kMax) * this.numFeatures / 100.);
            if (super.kMax < 5) {
                super.kMax = 5;
            }
        }
        //this.random = new Random(this.seed);
        //System.out.println("kmax: " + super.kMax);
        //System.out.println("is pct: " + this.isKmaxPct);
        //logger.info("initial solution: " + solution);
        //System.exit(-1);
        //System.out.println("kmax: " + super.kMax);
        //System.out.println("is pct: " + this.isKmaxPct);
        this.numFeatures = this.objectiveFunction.numberOfFeatures();
        return solution;
    }

    @Override
    public FSSolution shakeSolution(FSSolution currentSolution, int k) throws Exception {
        //logger.info("\tShake solution");
        //logger.info("\t\t[ss] k: " + k);
        List<Integer> in = new ArrayList<Integer>();
        int[] features = currentSolution.features();
        for (int f = 0; f < features.length; f++) {
            in.add(features[f]);
        }
        //int nbags = this.featureSpaceSearch.numberOfBags();
        int tnfeatures = this.numFeatures;
        //int cnfeatures = currentSolution.size();
        //logger.info("=====SHAKE SOLUTION METHOD=====");
        //logger.info("#: " + this.featureSpaceSearch);
        //logger.info("#features: " + in.size());
        //logger.info("#total features: " + tnfeatures);

        //logger.info("\t\t[ss] #features: " + in.size());
        boolean stop = false;
        for (int ik = 0; ik < k; ik++) {
            //logger.info("\t\t\t[ss] ik: " + ik + " (" + tnfeatures + ")");
            int pk = this.random.nextInt(tnfeatures);
            //logger.info("\t\t\t[ss] pk: " + pk);
            //logger.info("\t\t\t[ss] in: " + in);
            if (pk < in.size()) {
                int rfeature = in.remove(pk);
                //logger.info("\t\t\t[ss] removed feature: " + rfeature + " --> " + in);
                FeatureBags fbags = this.featureSpaceSearch.getBags(rfeature);
                if (fbags.numberOfBags() > 0) {
                    int[] feats = fbags.getAllFeatures();
                    pk = this.random.nextInt(feats.length);
                    //logger.info("\t\t\t\t\t[>ss] selected feature: " + fbags.getFeatureBag(pk));
                    if (!in.contains(feats[pk])) {
                        in.add(feats[pk]);
                    }
                }
            } else {
                stop = (in.size() < tnfeatures) ? false : true;
                while (!stop && in.size() < tnfeatures) {
                    pk = this.random.nextInt(tnfeatures);
                    //logger.info("feature to add: " + pk);
                    //logger.info("#total features: " + tnfeatures);
                    //logger.info("features available: " + in);
                    if (!in.contains(pk)) {
                        in.add(pk);
                        stop = true;
                    }
                }
            }
        }
        features = new int[in.size()];
        for (int f = 0; f < features.length; f++) {
            features[f] = in.get(f);
        }

        FSSolution sol = new FSSolution(this.numFeatures);
        sol.add(features);
        super.objectiveFunction.evaluate(sol);

        //logger.info("shaked solution" + sol.toString());
        //System.exit(-1);
        return sol;
    }
    
    /**
     * Local search: greedy combining SFS and SBE results stored in
     * sbvns/ls_greedy
     *
     * @param currentSolution
     * @return
     * @throws Exception
     */
    @Override
    public FSSolution localSearch(FSSolution currentSolution)
            throws Exception {
        FSSolution currentBestSolution = currentSolution.clone();
        super.objectiveFunction.evaluate(currentSolution);
        GreedyForwardSelectionFromSubset sbe = new GreedyForwardSelectionFromSubset(currentBestSolution.features());
        sbe.setObjectiveFunction(super.objectiveFunction);
        FSSolution sbe_solution = sbe.search();
        if (sbe_solution.isBetterThan(currentBestSolution)) {
            currentBestSolution = sbe_solution.clone();
        }
        GreedyBackwardSelectionFromSubset sfs = new GreedyBackwardSelectionFromSubset(currentBestSolution.features());
        sfs.setObjectiveFunction(super.objectiveFunction);
        FSSolution sfs_solution = sfs.search();
        //logger.info("=====LOCAL SEARCH=====");
        if (sfs_solution.isBetterThan(currentBestSolution)) {
            currentBestSolution = sfs_solution.clone();
        }
        return currentBestSolution;
    }


    @Override
    public FSPredGroupsBasicVNS clone() {
        FSPredGroupsBasicVNS algorithm = new FSPredGroupsBasicVNS();
        algorithm.setNumFeatures(this.numFeatures);
        algorithm.seed = this.seed;
        algorithm.random = new Random(this.seed);
        
        if (this.data != null) {
            algorithm.data = ArrayUtils.clone(this.data);
        }
        
        if (this.values != null) {
            algorithm.values = ArrayUtils.clone(this.values);
        }
        
        if (this.labels != null) {
            algorithm.labels = this.labels.clone();
        }
        
        algorithm.overlap = this.overlap;
        algorithm.isKmaxPct = this.isKmaxPct;
        algorithm.kMax = this.kMax;
        
        if (this.featureSpaceSearch != null) {
            algorithm.featureSpaceSearch = this.featureSpaceSearch.clone();
        }
       
        return algorithm;
    }

    
}
