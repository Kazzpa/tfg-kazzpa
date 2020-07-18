package es.kazzpa.selattserver.services;

import es.kazzpa.selattserver.models.Dataset;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface FileStorageService {
    Dataset storeFile(Authentication authentication, MultipartFile file);

    Resource loadFileAsResource(String fileName) throws Exception;

    List<Dataset> datasetsByUser(Authentication authentication) throws Exception;


}
