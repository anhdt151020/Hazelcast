package com.example.getdataservice1.Service;

import com.example.getdataservice1.Controller.PojoExample;
import com.example.getdataservice1.Entity.TransferModel;
import com.example.getdataservice1.Repository.TransferDataRepository;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransferDataServiceImpl implements TransferDataService{
    private final TransferDataRepository transferDataRepository;

    HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
//    private final RedisTemplate<Object, Object> template;
    Map<String, Object> dataExampleMap = hazelcastInstance.getMap("data-map");
    Map<String, Object> authData = hazelcastInstance.getMap("auth-map");

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
        Object value = authData.get(token);
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
}
