package es.kazzpa.selattserver.models;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@Table(name = "classifierResult")
public class ClassifierResult extends EntityDatabase {


    @ManyToOne
    private Algorithm algorithm;
    @ManyToOne
    private Dataset performed;

    private Date finishedDate;

    private boolean seen;

    @ManyToOne
    private Algorithm featureAlgorithm;

    //eval.numInstances()
    private Double numInstances;
    //eval.correct()
    private Double correctlyClassified;

    //eval.meanAbsoluteError()
    private Double meanAbsoluteError;


    public ClassifierResult() {
    }

    public ClassifierResult(Algorithm algorithm, Dataset performed) {
        this.algorithm = algorithm;
        this.performed = performed;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void setPerformed(Dataset performed) {
        this.performed = performed;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public Dataset getPerformed() {
        return performed;
    }

    public Date getFinishedDate() {
        return finishedDate;
    }


    public void setFinishedDate(Date finishedDate) {
        this.finishedDate = finishedDate;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public Double getNumInstances() {
        return numInstances;
    }

    public void setNumInstances(Double numInstances) {
        this.numInstances = numInstances;
    }

    public Double getCorrectlyClassified() {
        return correctlyClassified;
    }

    public void setCorrectlyClassified(Double correctlyClassified) {
        this.correctlyClassified = correctlyClassified;
    }

    public Double getMeanAbsoluteError() {
        return meanAbsoluteError;
    }

    public Algorithm getFeatureAlgorithm() {
        return featureAlgorithm;
    }

    public void setFeatureAlgorithm(Algorithm featureAlgorithm) {
        this.featureAlgorithm = featureAlgorithm;
    }

    public void setMeanAbsoluteError(Double meanAbsoluteError) {
        this.meanAbsoluteError = meanAbsoluteError;
    }
}
