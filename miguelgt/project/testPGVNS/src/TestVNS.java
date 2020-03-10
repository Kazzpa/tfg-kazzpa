
import java.io.File;
import upo.jcu.math.data.dataset.DataType;
import upo.jml.data.dataset.ClassificationDataset;
import upo.jml.data.dataset.DatasetUtils;
import upo.jml.data.dataset.ToyDatasets;
import upo.jml.prediction.classification.classifiers.BayesClassifier;
import upo.jml.prediction.classification.fss.algorithms.FSPredGroupsBasicVNS;
import upo.jml.prediction.classification.fss.core.FSObjectiveFunction;
import upo.jml.prediction.classification.fss.core.FSSolution;
import upo.jml.prediction.classification.fss.evaluators.CfsEvaluator;
import upo.jml.prediction.classification.fss.evaluators.WrapperEvaluator;

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
 * TestVNS.java
 * Copyright (C) 2008-2009 Miguel Garcia Torres
 */

/**
 *
 * @author mgarciat
 */
public class TestVNS {
        public static void testToyIris() throws Exception {
        int[][] data = ToyDatasets.iris_discrete;
        int[] labels = ToyDatasets.iris_labels;

        FSObjectiveFunction of = new CfsEvaluator(data, labels);
        of.buildEvaluator();
        FSPredGroupsBasicVNS bvns = new FSPredGroupsBasicVNS(data, labels, of);
        FSSolution solution = bvns.search();

        System.out.println(solution);
    }
    

    public static void testArff() throws Exception {
        String[] sdatasets = new String[] {"colon_tumor", "ionosphere", "glass"};
        String dpath = "data/" + sdatasets[0] + ".arff";
        ClassificationDataset dataset = DatasetUtils.loadArffDataset(new File(dpath), -1);
        if (!dataset.getDataType().equals(DataType.CATEGORICAL)) {
            dataset = DatasetUtils.dicretizeViaFayyad(dataset);
        }
        //ClassificationDataset ddataset = com.jscilib.math.data.dataset.DatasetUtils.dicretizeViaFayyad(dataset);
        //logger.info(ddataset.toString());

        FSObjectiveFunction of = new CfsEvaluator(dataset.getCategoricalData(), dataset.getLabels());
        of.buildEvaluator();
        FSPredGroupsBasicVNS bvns = new FSPredGroupsBasicVNS(dataset.getCategoricalData(), dataset.getLabels(), of, true);
        FSSolution solution = bvns.search();
        System.out.println(solution);
        //logger.info("best solution found: " + solution);
    }

    public static void main(String[] args) throws Exception {
        //TestVNS.testToyIris();
        TestVNS.testArff();
    }

}
