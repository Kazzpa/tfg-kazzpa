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
 * CMIM.java Copyright (C) 2008-2012 Miguel Garcia Torres
 */
package upo.jml.prediction.classification.fss.algorithms;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import upo.jcu.math.MatrixUtils;
import upo.jcu.math.stat.StatUtils;
import upo.jcu.utils.ArrayUtils;
import upo.jml.data.dataset.ClassificationDataset;
import upo.jml.data.dataset.DatasetUtils;
import upo.jml.prediction.classification.fss.core.FSObjectiveFunction;
import upo.jml.prediction.classification.fss.core.FSSolution;
import upo.jml.prediction.classification.fss.core.FeatureBag;
import upo.jml.prediction.classification.fss.core.SOFFSSearchAlgorithm;

/**
 * Fast Correlation Based Filter)
 *
 * @author Miguel Garcia Torres (mgarciat[at]upo[dot]es)
 */
public class FCBFOverlappingBagSearch implements SOFFSSearchAlgorithm {

    protected int[][] tdata = null;
    protected int[][] dvalues = null;
    protected int[] labels = null;
    protected int[] lvalues = null;
    protected double threshold = 0.;
    protected List<FeatureBag> bags = null;
    
    public FCBFOverlappingBagSearch() {}
    /**
     * Constructor.
     *
     * @param data
     * @param labels
     */
    public FCBFOverlappingBagSearch(int[][] data, int[] labels) {
        this(data, labels, 0.);
    }

    public FCBFOverlappingBagSearch(int[][] data, int[] labels, double thres) {
        this.setData(data);
        this.setLabels(labels);
        this.setThreshold(thres);
        this.bags = new ArrayList<FeatureBag>();
    }

    public FCBFOverlappingBagSearch(int[][] data, int[][] vals, int[] labels, double thres) {
        this.tdata = MatrixUtils.transpose(data);
        this.dvalues = vals;
        this.setLabels(labels);
        this.setThreshold(thres);
        this.bags = new ArrayList<FeatureBag>();
    }

    public FCBFOverlappingBagSearch(ClassificationDataset dataset, double thres) {
        this.setDataset(dataset);
        this.setThreshold(thres);
        this.bags = new ArrayList<FeatureBag>();
    }

    @Override
    public void setDataset(ClassificationDataset dataset) {
        this.tdata = MatrixUtils.transpose(dataset.getCategoricalData());
        this.labels = dataset.getLabels();
        //
        this.dvalues = new int[this.tdata.length][];
        for (int i = 0; i < this.tdata.length; i++) {
            this.dvalues[i] = dataset.getCategoricalValuesIndexes(i);//StatUtils.differentValues(this.tdata[i]);
        }
        //
        this.lvalues = dataset.getLabelIndexes();
    }

    public void setData(int[][] data) {
        this.tdata = MatrixUtils.transpose(data);
        this.dvalues = new int[this.tdata.length][];
        for (int i = 0; i < this.tdata.length; i++) {
            this.dvalues[i] = StatUtils.differentValues(this.tdata[i]);
        }
    }

    public void setLabels(int[] labels) {
        this.labels = labels;
        this.lvalues = StatUtils.differentValues(this.labels);
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public void setLogThreshold() {
    }

    @Override
    public FSSolution search() throws Exception {
        int nfeatures = this.tdata.length;
        //type 0 - unknown
        //type 1 - predominant
        //type 2 - redundant
        //calculate SUC(att, class_att) value
        int[] predominant = new int[nfeatures];
        double[] suc = new double[nfeatures];
        for (int i = 0; i < nfeatures; i++) {
            predominant[i] = 0;
            suc[i] = StatUtils.symmetricalUncertainty(this.tdata[i], this.dvalues[i].length, this.labels, this.lvalues.length);
        }

        int[] isuc = ArrayUtils.sort(suc);

        boolean debug = false;
        //remove redundant features
        int att1, att2;
        FSSolution bestSolution = new FSSolution(this.tdata.length);
        double wvalue = 0.;
        double sux = Double.NaN;

        int index1 = nfeatures - 1;
        att1 = isuc[index1];
        while (index1 > 0 && suc[att1] > this.threshold) {
            if (predominant[att1] == 0) {
                predominant[att1] = 1;
                FeatureBag bag = new FeatureBag(att1);
                bestSolution.add(att1);
                wvalue += suc[att1];
                int index2 = nfeatures - 2;
                att2 = isuc[index2];
                while (index2 > 0 && suc[att2] > this.threshold) {
                    if (predominant[att2] != 1) {
                        sux = StatUtils.symmetricalUncertainty(this.tdata[att1], this.dvalues[att1].length, this.tdata[att2], this.dvalues[att2].length);
                        if (sux >= suc[isuc[index2]]) {
                            predominant[att2] = 2;
                            bag.addFeatureToBag(att2);
                        }
                    }
                    index2--;
                    att2 = isuc[index2];
                }
                this.bags.add(bag);
            }
            index1--;
            att1 = isuc[index1];
        }

        bestSolution.setValue(wvalue / bestSolution.size());
        return bestSolution;
    }

    public List<FeatureBag> getBags() {
        return this.bags;
    }

    public static void main(String[] args) throws Exception {
        //data
        //int[][] data = DatasetUtils.iris_discrete;
        //int[] labels = DatasetUtils.iris_labels;

        String dataset = "wine";
        String path = "/inv/ldi/md/cdd/" + dataset + ".arff";
        ClassificationDataset cdataset = DatasetUtils.loadArffDataset(new File(path), -1);
        ClassificationDataset ndataset = DatasetUtils.dicretizeViaFayyad(cdataset);
        int[][] data = ndataset.getCategoricalData();
        int[] labels = ndataset.getLabels();


        FCBFOverlappingBagSearch algorithm = new FCBFOverlappingBagSearch(data, labels, 0.);
        FSSolution solution = algorithm.search();
        System.out.println("solution: " + solution);
        List<FeatureBag> bags = algorithm.getBags();
        System.out.println("#bags: " + bags.size());
        for (int i = 0; i < bags.size(); i++) {
            System.out.println(bags.get(i));
        }
    }

    @Override
    public void setObjectiveFunction(FSObjectiveFunction of) throws Exception {
        throw new UnsupportedOperationException("No o.f. is necessary"); 
    }

    @Override
    public FSObjectiveFunction getObjectiveFunction() throws Exception {
        return null;
    }

    @Override
    public FCBFOverlappingBagSearch clone() {
        FCBFOverlappingBagSearch search = new FCBFOverlappingBagSearch();
        search.setThreshold(this.threshold);
        
        
        if (this.tdata != null) {
            search.tdata = ArrayUtils.clone(this.tdata);
        }
        
        if (this.dvalues != null) {
            search.dvalues = ArrayUtils.clone(this.dvalues);
        }
        
        if (this.labels != null) {
            search.labels = this.labels.clone();
        }
        
        if (this.lvalues != null) {
            search.lvalues = this.lvalues.clone();
        }
    
        if (this.bags != null) {
            search.bags = new ArrayList<FeatureBag>();
            for (int i = 0; i < this.bags.size(); i++) {
                search.bags.add(this.bags.get(i).clone());
            }
        }
        return search;
    }
}
