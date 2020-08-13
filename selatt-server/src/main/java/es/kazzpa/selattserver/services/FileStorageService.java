package es.kazzpa.selattserver.services;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;


public interface FileStorageService {
    ResponseEntity storeFile(Authentication authentication, MultipartFile file) throws Exception;

    Resource loadFileAsResource(String fileName) throws Exception;


}
