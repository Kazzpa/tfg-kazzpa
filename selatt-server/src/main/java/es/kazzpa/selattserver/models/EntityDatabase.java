package es.kazzpa.selattserver.models;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.util.UUID;
@MappedSuperclass
public abstract class EntityDatabase implements DBEntity{
    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @PrePersist
    public void onSave(){
        this.id = UUID.randomUUID().toString();
    }
}
