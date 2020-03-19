package es.kazzpa.selattserver.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "dataset")
public class Dataset implements Serializable {
    @Id
    // @GeneratedValue(strategy=GenerationType.AUTO)
    private String uuid;

    private String filePath;
    @ManyToOne
    private Usuario uploader;
    private String name;
    private int nrow;
    private int ncol;

    public Dataset() {

    }

    public Dataset(Usuario uploader) {
        this.uploader = uploader;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Usuario getUploader() {
        return uploader;
    }

    public void setUploader(Usuario uploader) {
        this.uploader = uploader;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNrow() {
        return nrow;
    }

    public void setNrow(int nrow) {
        this.nrow = nrow;
    }

    public int getNcol() {
        return ncol;
    }

    public void setNcol(int ncol) {
        this.ncol = ncol;
    }
}
