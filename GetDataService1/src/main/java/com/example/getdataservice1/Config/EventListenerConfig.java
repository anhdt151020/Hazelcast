package com.example.getdataservice1.Config;

import com.example.getdataservice1.Entity.DataMakerModel;
import com.example.getdataservice1.Entity.MakeDataEvent;
import com.example.getdataservice1.Entity.SavingFailedDataEvent;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventListenerConfig {
    private final ApplicationEventPublisher publisher;
    HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
    IMap<String, String> eventMap = hazelcastInstance.getMap("event-map");

    IMap<String, Object> dataExampleMap = hazelcastInstance.getMap("data-map");

    @EventListener
    public void makeDataEventListener(MakeDataEvent makeDataEvent) throws InterruptedException {
        log.info("Event listened!!!");
        String eventKey = "event_" + makeDataEvent.getDataMakerModel().getId().toString() + "_" + makeDataEvent.getDataMakerModel().getUsername();
        eventMap.put(eventKey, makeDataEvent.getDataMakerModel().getStatus(), 180, TimeUnit.SECONDS);
        log.info("push event to hazelcast with key-value: {}-{}", eventKey, makeDataEvent.getDataMakerModel().getStatus());

        Thread.sleep(180000);

        String status = eventMap.get(eventKey);
        log.info("status: {}", status);


        if ("1".equals(status)){
            publisher.publishEvent(new SavingFailedDataEvent(this, makeDataEvent.getDataMakerModel()));
        }
    }
}
