package es.kazzpa.selattserver.models;

import javax.persistence.*;
import javax.xml.transform.Result;
import javax.persistence.Entity;
import java.util.List;

@Entity
@Table(name = "result_filter")
public class ResultFilter {
    @Id
    @Column(name="result_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    @ManyToOne
    private Algorithm algorithm;
    @ManyToOne
    private Dataset performed;

    public ResultFilter() {
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public void setPerformed(Dataset performed) {
        this.performed = performed;
    }
}
