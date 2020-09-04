package es.kazzpa.selattserver.controllers;

import es.kazzpa.selattserver.services.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/fileManager")
public class FileManagerController {


    private final FileStorageService fileStorageService;

    public FileManagerController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }


    @PostMapping(path = "uploadDataset", consumes = "multipart/form-data")
    public ResponseEntity loadDataset(Authentication authentication, @RequestParam("file") MultipartFile file) throws Exception {
        return fileStorageService.storeFile(authentication, file);
    }


}
