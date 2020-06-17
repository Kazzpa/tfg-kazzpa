package es.kazzpa.selattserver.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {
    private String nombre;
    @Id
    private String uuid;

    public Usuario() {
    }

    public Usuario(String nombre) {
        this.nombre = nombre;
    }
}
