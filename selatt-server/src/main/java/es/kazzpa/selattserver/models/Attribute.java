package es.kazzpa.selattserver.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "attribute")
public class Attribute implements Serializable {

    @Id
    private int index;
    @ManyToOne
    //@Id
    private ResultFilter dataPerformed;
    private Double value;

    public Attribute() {
    }

    public ResultFilter getDataPerformed() {
        return dataPerformed;
    }

    public void setDataPerformed(ResultFilter dataPerformed) {
        this.dataPerformed = dataPerformed;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

}
