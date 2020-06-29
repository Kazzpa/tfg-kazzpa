package es.kazzpa.selattserver.models;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "dataset")
public class Dataset extends EntityDatabase {


    private String filename;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Usuario uploader;
    private String name;
    private String fileDownloadUri;
    private String fileType;
    private long size;

    public Dataset(){

    }
    public Dataset(String filename, String fileDownloadUri, String fileType, long size) {
        this.filename = filename;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
    }

    public Dataset(Usuario uploader) {
        this.uploader = uploader;
    }


    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
