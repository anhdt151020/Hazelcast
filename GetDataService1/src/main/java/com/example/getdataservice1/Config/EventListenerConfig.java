package com.example.getdataservice1.Config;

import com.example.getdataservice1.Entity.DataMakerModel;
import com.example.getdataservice1.Entity.MakeDataEvent;
import com.example.getdataservice1.Entity.SavingFailedDataEvent;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.rabbitmq.client.AMQP;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
@EnableAsync
public class EventListenerConfig {
    private final RabbitTemplate rabbitTemplate;
    HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
    IMap<String, String> eventMap = hazelcastInstance.getMap("event-map");
    public static final String EXCHANGE = "Data_Exchange";
    public static final String ROUTING_KEY = "Data_RoutingKey";

    @EventListener
    @Async
    public void makeDataEventListener(MakeDataEvent makeDataEvent) throws InterruptedException {
        log.info("Event listened!!!");
        String eventKey = "event_" + makeDataEvent.getDataMakerModel().getId().toString() + "_" + makeDataEvent.getDataMakerModel().getUsername();
        eventMap.put(eventKey, makeDataEvent.getDataMakerModel().getStatus(), 200, TimeUnit.SECONDS);
        log.info("push event status data to hazelcast with key: {} - status: {}", eventKey, makeDataEvent.getDataMakerModel().getStatus());

        wait(180000);

        String status = eventMap.get(eventKey);
        log.info("status: {}", status);


        if ("1".equals(status)){
            log.info("Data process failed !");
            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, makeDataEvent.getDataMakerModel());
            log.info("publish event to rabbit message queue with exchange: {}, routing key: {}, makeDataEvent: {}", EXCHANGE, ROUTING_KEY, makeDataEvent.getDataMakerModel());
        }
        if ("3".equals(status)){
            log.info("Data process success!, No more Rabbit, Bye!");
        }
    }
}
