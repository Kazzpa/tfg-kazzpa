package es.kazzpa.selattserver.models;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@Table(name = "result_filter")
public class ResultFilter extends EntityDatabase{


    @ManyToOne
    private Algorithm algorithm;
    @ManyToOne
    private Dataset performed;

    private Date finishedDate;

    //TODO: HACER UN JSON A PARTIR DE LOS ATRIBUTOS PARSEARLO AL SACARLO DE LA DB Y DEMAS PARA TRABAJARLO.
    private String jsonAttributes;

    private long scoreVNS;


    public ResultFilter() {
    }

    public ResultFilter(Algorithm algorithm, Dataset performed) {
        this.algorithm = algorithm;
        this.performed = performed;
    }


    public String getJsonAttributes() {
        return jsonAttributes;
    }

    public void setJsonAttributes(String jsonAtributes) {
        this.jsonAttributes = jsonAtributes;
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

    public Date getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(Date finishedDate) {
        this.finishedDate = finishedDate;
    }
}
