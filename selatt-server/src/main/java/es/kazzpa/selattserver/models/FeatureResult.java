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

    @Column(length = 1024)
    private String attributesSelected;

    private Date finishedDate;

    public Dataset getPerformed() {
        return performed;
    }

    public void setPerformed(Dataset originalDataset) {
        this.performed = originalDataset;
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
}
