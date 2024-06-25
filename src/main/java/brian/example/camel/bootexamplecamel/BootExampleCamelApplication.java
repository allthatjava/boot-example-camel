package brian.example.camel.bootexamplecamel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        org.apache.camel.spring.boot.security.CamelSSLAutoConfiguration.class
})
public class BootExampleCamelApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootExampleCamelApplication.class, args);
    }

}
