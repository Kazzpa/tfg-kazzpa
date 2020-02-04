package es.kazzpa.selattserver.models;

import javax.xml.transform.Result;

public class ResultFilter {
    private String filterName;
    private String resultTest;
    private int[] selectedAtr;

    public ResultFilter(){
        this.filterName = "Undefined";
        this.resultTest = "Undefined";
        this.selectedAtr = new int[10];
    }

    public String getFilterName() {
        return filterName;
    }

    public String getResultTest() {
        return resultTest;
    }

    public int[] getSelectedAtr() {
        return selectedAtr;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public void setResultTest(String resultTest) {
        this.resultTest = resultTest;
    }

    public void setSelectedAtr(int[] selectedAtr) {
        this.selectedAtr = selectedAtr;
    }
}
