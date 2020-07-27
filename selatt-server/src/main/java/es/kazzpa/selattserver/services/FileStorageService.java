package es.kazzpa.selattserver.services;

import es.kazzpa.selattserver.models.Dataset;
import es.kazzpa.selattserver.models.ResultFilter;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface FileStorageService {
    Dataset storeFile(Authentication authentication, MultipartFile file) throws Exception;

    Resource loadFileAsResource(String fileName) throws Exception;


}
