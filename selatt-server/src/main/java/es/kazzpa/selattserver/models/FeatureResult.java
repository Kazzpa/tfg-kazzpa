package es.kazzpa.selattserver.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "feature_result")
public class FeatureResult extends EntityDatabase{


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Algorithm algorithm;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Dataset performed;

    @Column(length = 2048)
    private String attributesSelected;

    private int numAttributes;

    private Date finishedDate;

    private boolean seen;

    public Dataset getPerformed() {
        return performed;
    }

    public void setPerformed(Dataset performed) {
        this.performed = performed;
    }

    public String getAttributesSelected() {
        return attributesSelected;
    }

    public void setAttributesSelected(String attributesSelected) {
        this.attributesSelected = attributesSelected;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public Date getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(Date finishedDate) {
        this.finishedDate = finishedDate;
    }

    public int getNumAttributes() {
        return numAttributes;
    }

    public void setNumAttributes(int numAttributes) {
        this.numAttributes = numAttributes;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    @Override
    public String toString() {
        return "FeatureResult{" +
                "algorithm=" + algorithm +
                ", performed=" + performed +
                ", attributesSelected='" + attributesSelected + '\'' +
                ", finishedDate=" + finishedDate +
                '}';
    }
}
