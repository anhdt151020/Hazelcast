package com.example.getdataservice1;

import com.example.getdataservice1.Config.HazelcastEntryListenerConfiguration;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
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

        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
        IMap<String, Object> dataExampleMap = hazelcastInstance.getMap("data-map");

        dataExampleMap.addEntryListener(new HazelcastEntryListenerConfiguration(), true);


    }

}
