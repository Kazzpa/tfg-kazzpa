package es.kazzpa.selattserver;

import es.kazzpa.selattserver.properties.Properties;
import es.kazzpa.selattserver.services.FileStorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableConfigurationProperties({
        Properties.class
})
public class SelattServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SelattServerApplication.class, args);
    }


}
