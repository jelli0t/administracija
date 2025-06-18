package rs.neks.administration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AdministrationApi {

    public static void main(String... args) {
        SpringApplication.run(
                AdministrationApi.class,
                args
        );
    }
}
