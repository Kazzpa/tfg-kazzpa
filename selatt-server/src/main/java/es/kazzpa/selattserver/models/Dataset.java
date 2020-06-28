package es.kazzpa.selattserver.models;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "dataset")
public class Dataset extends EntityDatabase {


    private String filePath;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Usuario uploader;
    private String name;
    private int nrow;
    private int ncol;

    public Dataset() {

    }

    public Dataset(Usuario uploader) {
        this.uploader = uploader;
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
