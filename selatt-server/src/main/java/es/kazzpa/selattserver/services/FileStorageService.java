package es.kazzpa.selattserver.services;

import es.kazzpa.selattserver.models.Dataset;
import es.kazzpa.selattserver.models.ResultFilter;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.crypto.Data;
import java.util.List;


public interface FileStorageService {
    ResponseEntity storeFile(Authentication authentication, MultipartFile file) throws Exception;

    Resource loadFileAsResource(String fileName) throws Exception;


}
