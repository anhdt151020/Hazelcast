package com.example.getdataservice1.Service;

import com.example.getdataservice1.Entity.DataExample;
import com.example.getdataservice1.Entity.DataMakerModel;
import com.example.getdataservice1.Entity.MakeDataEvent;
import com.example.getdataservice1.Entity.TransferModel;
import com.example.getdataservice1.Repository.TransferDataRepository;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.impl.AMQImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferDataServiceImpl implements TransferDataService{
    private final TransferDataRepository transferDataRepository;
    private final ApplicationEventPublisher publisher;
    private final RabbitTemplate rabbitTemplate;
    HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
    IMap<String, Object> dataExampleMap = hazelcastInstance.getMap("data-map");
    IMap<Long, String> dataStringMap = hazelcastInstance.getMap("data-map");
    Map<String, Object> authMap = hazelcastInstance.getMap("auth-map");

    @Override
    public TransferModel putNewData(TransferModel transferModel) {
        dataExampleMap.put(transferModel.getTransKey(), transferModel);
        return transferModel;
    }

    @Override
    public TransferModel putAndSaveNewData(TransferModel transferModel) {
        dataExampleMap.put(transferModel.getTransKey(), transferModel);
        return transferDataRepository.save(transferModel);
    }

    @Override
    public TransferModel update(TransferModel transferModel) {
        TransferModel model = (TransferModel) dataExampleMap.get(transferModel.getTransKey());
        if (model == null){
            throw new RuntimeException("invalid Data Model");
        } else {
            model.setTransValue(transferModel.getTransValue());
            dataExampleMap.replace(model.getTransKey(), model);
        }
        return model;
    }

    @Override
    public TransferModel getData(String key) throws InterruptedException {
        TransferModel model = (TransferModel) dataExampleMap.get(key);
        if (model == null){
            Thread.sleep(5000);
            model = transferDataRepository.getByTransKey(key);
        }
        return model;
    }

    @Override
    public TransferModel saveKey(String key) {
        TransferModel model = (TransferModel) dataExampleMap.get(key);
        if (model == null){
            throw new RuntimeException("Invalid Key");
        }
        return transferDataRepository.save(model);
    }

    @Override
    public void makeData(HttpServletRequest request, DataMakerModel dataMakerModel) {
        log.info("make data with model {}", dataMakerModel);

        String token = null;
        final String authorizationHeaderValue = request.getHeader("Authorization");
        if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer")) {
            token = authorizationHeaderValue.substring(7);
        }

        Object value = authMap.get(token);
        log.info("username = {}", value);

        if (value == null){
            throw new RuntimeException("Token invalid");
        } else {

            log.info("value not null");
            if (!value.toString().equals(dataMakerModel.getUsername())){
                throw new RuntimeException("Permission denied !");
            }

            String key = "data_" + dataMakerModel.getUsername();

            log.info("push data with key: {}", key);

            dataExampleMap.put(key, dataMakerModel, 300, TimeUnit.SECONDS);
            publisher.publishEvent(new MakeDataEvent(this, dataMakerModel));
        }
    }
}
