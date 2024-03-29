package com.example.patientservice;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableEurekaClient
@EnableEncryptableProperties
@EnableFeignClients
public class PatientServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PatientServiceApplication.class, args);
    }
}
