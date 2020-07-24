package es.kazzpa.selattserver.models;

import org.hibernate.annotations.CreationTimestamp;
import org.w3c.dom.Attr;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "result_filter")
public class ResultFilter extends EntityDatabase {

    @ManyToOne
    private Algorithm algorithm;
    @ManyToOne
    private Dataset performed;
    private long scoreVNS;


    public ResultFilter() {
    }

    public ResultFilter(Algorithm algorithm, Dataset performed) {
        this.algorithm = algorithm;
        this.performed = performed;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void setPerformed(Dataset performed) {
        this.performed = performed;
    }

    public long getScoreVNS() {
        return scoreVNS;
    }

    public void setScoreVNS(long scoreVNS) {
        this.scoreVNS = scoreVNS;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public Dataset getPerformed() {
        return performed;
    }

}
