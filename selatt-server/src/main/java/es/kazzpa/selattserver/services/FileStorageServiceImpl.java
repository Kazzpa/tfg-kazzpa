package es.kazzpa.selattserver.services;

import es.kazzpa.selattserver.models.AppUser;
import es.kazzpa.selattserver.models.Dataset;
import es.kazzpa.selattserver.models.ResultFilter;
import es.kazzpa.selattserver.properties.Properties;
import es.kazzpa.selattserver.repositories.AppUserRepository;
import es.kazzpa.selattserver.repositories.DatasetRepository;
import es.kazzpa.selattserver.repositories.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.xml.transform.Result;
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
    public Dataset storeFile(Authentication authentication, MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (filename.contains("..")) {
                throw new Exception("Ruta de archivo invalida");
            }
            Path targetLocation = this.fileStorageLocation.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(filename)
                    .toUriString();
            AppUser appUser = appUserRepository.findByUsername(authentication.getName());
            Dataset dataset = new Dataset(filename, fileDownloadUri, file.getContentType(), file.getSize(), appUser);
            datasetRepository.save(dataset);
            return dataset;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
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
