package es.kazzpa.selattserver.controllers;

import es.kazzpa.selattserver.models.Dataset;
import es.kazzpa.selattserver.services.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

    @PostMapping(path = "uploadDataset", headers = ("content-type=multipart/form-data"))
    public Dataset loadDataset(@RequestParam("file") MultipartFile file) {
        String filename = fileStorageService.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(filename)
                .toUriString();
        return new Dataset(filename, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List<Dataset> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> loadDataset(file))
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/")
    public ResponseEntity<Resource> downloadFile(@RequestParam String filename, HttpServletRequest request) throws Exception {
        //load file as a resource
        Resource resource = fileStorageService.loadFileAsResource(filename);

        //Try to determine file content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            throw new Exception("Could not determine file type" + filename + ex.getMessage());
        }catch(NullPointerException ex){
            throw new Exception("Couldn't locate the file " + filename + ex.getMessage());
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment, filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


}
