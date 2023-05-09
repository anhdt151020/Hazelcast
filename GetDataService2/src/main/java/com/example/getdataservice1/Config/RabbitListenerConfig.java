package com.example.getdataservice1.Config;

import com.example.getdataservice1.Entity.DataMakerModel;
import com.example.getdataservice1.Repository.DataMakerModelRepository;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@EnableAsync
@RabbitListener(queues = "Data_Queue", id = "listener")
public class RabbitListenerConfig {

    private final DataMakerModelRepository dataMakerModelRepository;
    @RabbitHandler
    public void receiver(DataMakerModel dataMakerModel) {
        log.info("Save Data for failed Process: {}", dataMakerModel);
        dataMakerModelRepository.save(dataMakerModel);
    }
}
