package es.kazzpa.selattserver.models;

import javax.xml.transform.Result;

public class ResultFilter {
    private String filterName;
    private int[] selectedAtr;
    private double[][] rankedAtr;
    private double[] auxdouble;
    private double[] auxdouble2;
    public ResultFilter(){
        this.filterName = "Undefined";
        this.selectedAtr = new int[10];
        this.rankedAtr = new double[10][10];
    }

    public String getFilterName() {
        return filterName;
    }

    public double[][] getRankedAtr() {
        return rankedAtr;
    }

    public int[] getSelectedAtr() {
        return selectedAtr;
    }

    public double[] getAuxdouble() {
        return auxdouble;
    }

    public double[] getAuxdouble2() {
        return auxdouble2;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public void setRankedAtr(double[][] rankedAtr) {
        this.rankedAtr = rankedAtr;
    }
    public void setAuxdouble(double[] auxdouble) {
        this.auxdouble = auxdouble;
    }
    public void setAuxdouble2(double[] auxdouble2) {
        this.auxdouble2 = auxdouble2;
    }
    public void setSelectedAtr(int[] selectedAtr) {
        this.selectedAtr = selectedAtr;
    }
}
