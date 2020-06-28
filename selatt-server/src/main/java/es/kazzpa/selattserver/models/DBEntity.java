package es.kazzpa.selattserver.models;

import javax.persistence.PrePersist;
import java.io.Serializable;

public interface DBEntity extends Serializable {
    String getId();

    void setId(String id);

    void onSave();

}
