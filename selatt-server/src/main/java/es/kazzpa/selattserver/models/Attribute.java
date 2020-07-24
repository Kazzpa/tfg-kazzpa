package es.kazzpa.selattserver.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "attribute")
public class Attribute implements Serializable{

    @Id
    private int id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public ResultFilter dataPerformed;


    private Double value;

    public Attribute() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public ResultFilter getDataPerformed() {
        return dataPerformed;
    }

    public void setDataPerformed(ResultFilter dataPerformed) {
        this.dataPerformed = dataPerformed;
    }
}
