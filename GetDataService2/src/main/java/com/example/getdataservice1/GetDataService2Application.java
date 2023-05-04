package com.example.getdataservice1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableCaching
public class GetDataService2Application {

    public static void main(String[] args) {
        SpringApplication.run(GetDataService2Application.class, args);
    }

}
