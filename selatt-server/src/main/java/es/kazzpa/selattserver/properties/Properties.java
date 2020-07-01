package es.kazzpa.selattserver.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="dataset")
public class Properties {
    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        String path = System.getProperty("user.dir") + uploadDir;
        this.uploadDir = path;
    }
}