package com.example.getdataservice1.Service;

import com.example.getdataservice1.Controller.PojoExample;
import com.example.getdataservice1.Entity.DataMakerModel;
import com.example.getdataservice1.Entity.TransferModel;
import com.example.getdataservice1.Repository.DataMakerModelRepository;
import com.example.getdataservice1.Repository.TransferDataRepository;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferDataServiceImpl implements TransferDataService{
    private final TransferDataRepository transferDataRepository;
    private final DataMakerModelRepository dataMakerModelRepository;

    HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
//    private final RedisTemplate<Object, Object> template;
    Map<String, Object> dataExampleMap = hazelcastInstance.getMap("data-map");
    Map<String, Object> authMap = hazelcastInstance.getMap("auth-map");
    IMap<String, String> eventMap = hazelcastInstance.getMap("event-map");

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
    public PojoExample getData(HttpServletRequest request, String key) throws InterruptedException {
        String token = null;
        PojoExample res;
        final String authorizationHeaderValue = request.getHeader("Authorization");
        if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer")) {
            token = authorizationHeaderValue.substring(7);
        }
        assert token != null;
        Object value = authMap.get(token);
        if (value == null){
            throw new RuntimeException("Token invalid");
        } else {
            TransferModel model = (TransferModel) dataExampleMap.get(key);
            if (model == null) {
                Thread.sleep(5000);
                model = transferDataRepository.getByTransKey(key);
            }
             res = PojoExample.builder()
                    .key(model.getTransKey())
                    .value(model.getTransValue())
                    .username(value.toString())
                    .SessionId(request.getSession().getId())
                    .build();

        }
        return res;
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
    public DataMakerModel getMakedData(HttpServletRequest request, Boolean flag) {
        DataMakerModel res;
        String token = null;
        final String authorizationHeaderValue = request.getHeader("Authorization");
        if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer")) {
            token = authorizationHeaderValue.substring(7);
        }
        log.info("get data with token {}", token);

        Object value = authMap.get(token);
        log.info("username = {}", value);

        if (value == null){
            throw new RuntimeException("Token invalid");
        } else {
            log.info("value not null");
            String key = "data_" + value.toString();
            log.info("get data with key: {}", key);

            res = (DataMakerModel) dataExampleMap.get(key);
            // TODO
            if (flag) {
                dataExampleMap.remove(key);
            }
        }
        return res;
    }

    @Override
    public DataMakerModel updateMakedData(HttpServletRequest request, DataMakerModel dataMakerModel) {
        DataMakerModel temp = getMakedData(request, Boolean.TRUE);
        if (temp == null){
            throw new RuntimeException("invalid data!");
        }
        log.info("Current Data in cache: {}", temp);

        if (!temp.getUsername().equals(dataMakerModel.getUsername())) {
            throw new RuntimeException("invalid username data!");
        }

        if (!temp.getData().equals(dataMakerModel.getData())){
            throw new RuntimeException("invalid username data!");
        }

        temp.setStatus(dataMakerModel.getStatus());

        log.info("Status: {}, process successful", dataMakerModel.getStatus());

        String eventKey = "event_" + dataMakerModel.getId().toString() + "_" + dataMakerModel.getUsername();
        eventMap.put(eventKey, dataMakerModel.getStatus(), 200, TimeUnit.SECONDS);

        return dataMakerModelRepository.save(temp);
    }
}
