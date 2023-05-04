package com.example.getdataservice1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableCaching
public class GetDataService1Application {

    public static void main(String[] args) {
        SpringApplication.run(GetDataService1Application.class, args);
    }

}
