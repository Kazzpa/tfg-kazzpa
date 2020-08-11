package es.kazzpa.selattserver.services;

import es.kazzpa.selattserver.models.AppUser;
import es.kazzpa.selattserver.models.Dataset;
import es.kazzpa.selattserver.properties.Properties;
import es.kazzpa.selattserver.repositories.AppUserRepository;
import es.kazzpa.selattserver.repositories.DatasetRepository;
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

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FileStorageServiceImpl implements FileStorageService {


    private final Path fileStorageLocation;
    private final String uploadDir;
    @Autowired
    private DatasetRepository datasetRepository;
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    public FileStorageServiceImpl(Properties properties) {
        this.fileStorageLocation = Paths.get(properties.getUploadDir())
                .normalize();
        this.uploadDir = properties.getUploadDir();
        try {
            Files.createDirectories(this.fileStorageLocation);

        } catch (Exception ex) {
            System.out.println("Error al crear el directorio" + fileStorageLocation);
        }
    }

    @Override
    public ResponseEntity storeFile(Authentication authentication, MultipartFile file) throws Exception {

        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        String path = uploadDir + "\\" + format.format(now) + "_" + file.getOriginalFilename();
        String filePath = StringUtils.cleanPath(System.getProperty("user.dir") + path);
        System.out.println(filePath);
        System.out.println(path);

        if (filePath.contains("..")) {
            return ResponseEntity.badRequest().body("Ruta de archivo invalida");
        }
        Path targetLocation = this.fileStorageLocation.resolve(filePath);
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
                mimeType = ext;
                if (ext.equals("arff")) {
                    break;
                } else {
                    fileTest.delete();
                    return ResponseEntity.badRequest().body("Unsupported File Type : " + fileTest.getName() + " Extension:" + ext);
                }
        }

        String fileDownloadUri = path;
        System.out.println(filePath);
        AppUser appUser = appUserRepository.findByUsername(authentication.getName());
        Dataset dataset = new Dataset(file.getOriginalFilename(), fileDownloadUri, mimeType, file.getSize(), appUser);
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
