package es.kazzpa.selattserver.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "dataset", uniqueConstraints = @UniqueConstraint(columnNames = {"filename","fileDownloadUri"}))
public class Dataset extends EntityDatabase {

    private String filename;
    private String name;
    private String fileDownloadUri;
    private String fileType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AppUser userUploader;
    private long size;

    public Dataset() {

    }

    public Dataset(String filename, String fileDownloadUri, String fileType, long size, AppUser userUploader) {
        this.filename = filename;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
        this.userUploader = userUploader;
    }


    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
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

    public AppUser getUserUploader() {
        return userUploader;
    }

    public void setUserUploader(AppUser userUploader) {
        this.userUploader = userUploader;
    }

    @Override
    public String toString() {
        return "Dataset{" +
                "filename='" + filename + '\'' +
                ", name='" + name + '\'' +
                ", fileDownloadUri='" + fileDownloadUri + '\'' +
                ", fileType='" + fileType + '\'' +
                ", userUploader=" + userUploader +
                ", size=" + size +
                '}';
    }
}
