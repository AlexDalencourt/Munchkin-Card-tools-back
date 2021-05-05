package munchkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
//@EnableJpaRepositories
@EntityScan({"munchkin.integrator.infrastructure.repositories.entities"})
//@ComponentScan(basePackageClasses = {"munchkin.integrator"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
