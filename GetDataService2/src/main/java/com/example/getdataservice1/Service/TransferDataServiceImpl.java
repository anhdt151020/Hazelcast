package com.example.getdataservice1.Service;

import com.example.getdataservice1.Entity.TransferModel;
import com.example.getdataservice1.Repository.TransferDataRepository;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransferDataServiceImpl implements TransferDataService{
    private final TransferDataRepository transferDataRepository;

    HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
    Map<String, Object> dataExampleMap = hazelcastInstance.getMap("example-map");

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
}
