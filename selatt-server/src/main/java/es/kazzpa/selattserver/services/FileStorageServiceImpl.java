package es.kazzpa.selattserver.services;

import es.kazzpa.selattserver.models.AppUser;
import es.kazzpa.selattserver.models.Dataset;
import es.kazzpa.selattserver.models.ResultFilter;
import es.kazzpa.selattserver.properties.Properties;
import es.kazzpa.selattserver.repositories.AppUserRepository;
import es.kazzpa.selattserver.repositories.DatasetRepository;
import es.kazzpa.selattserver.repositories.ResultRepository;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.xml.transform.Result;
import javax.xml.ws.Response;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class FileStorageServiceImpl implements FileStorageService {


    private final Path fileStorageLocation;
    @Autowired
    private DatasetRepository datasetRepository;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    public FileStorageServiceImpl(Properties properties) {
        this.fileStorageLocation = Paths.get(properties.getUploadDir())
                .toAbsolutePath()
                .normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);

        } catch (Exception ex) {
            System.out.println("Error al crear el directorio" + fileStorageLocation);
        }
    }

    @Override
    public ResponseEntity storeFile(Authentication authentication, MultipartFile file) throws Exception {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        if (filename.contains("..")) {
            return ResponseEntity.badRequest().body("Ruta de archivo invalida");
        }
        Path targetLocation = this.fileStorageLocation.resolve(filename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        //TODO: FIX LOADING FROM .JSON
        File fileTest = new File(targetLocation.toUri());
        String mimeType = new Tika().detect(fileTest);

        switch (mimeType) {
            case "text/csv":
                break;
            case "application/json":
                break;
            default:
                String ext = FilenameUtils.getExtension(fileTest.getName());
                if (ext.equals("arff")) {
                    break;
                } else {
                    fileTest.delete();
                    return ResponseEntity.badRequest().body("Unsupported File Type : " + fileTest.getName() + " Extension:" + ext);
                }
        }
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(filename)
                .toUriString();
        AppUser appUser = appUserRepository.findByUsername(authentication.getName());
        Dataset dataset = new Dataset(filename, fileDownloadUri, file.getContentType(), file.getSize(), appUser);
        datasetRepository.save(dataset);

        return ResponseEntity.ok(dataset);
    }

    @Override
    public Resource loadFileAsResource(String fileName) throws Exception {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new Exception("Resource not found " + fileName);
            }
        } catch (Exception ex) {
            throw new Exception("Error: " + ex.getMessage());
        }
    }


}
