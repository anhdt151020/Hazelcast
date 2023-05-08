package com.example.getdataservice1.Service;

import com.example.getdataservice1.Entity.DataExample;
import com.example.getdataservice1.Repository.DataExampleRepository;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataExampleServiceImpl implements DataExampleService{

    private final DataExampleRepository dataExampleRepository;
//    private final RedisTemplate<Object, Object> template;

    HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
    IMap<String, Object> myMap = hazelcastInstance.getMap("auth-map");
    IMap<Long, Object> dataMap = hazelcastInstance.getMap("data-map");
    @Override
    public DataExample saveNewRecord(DataExample dataExample) {
        return dataExampleRepository.save(dataExample);
    }

    @Cacheable(value = "dataExample")
    @Override
    public List<DataExample> getDataByToken(String token) throws InterruptedException {
        Thread.sleep(5000);
        Object value = myMap.get(token);
        if (value == null){
            throw new RuntimeException("Token invalid");
        } else {
            log.info("value not null");
            String username = value.toString();
            return dataExampleRepository.findAllByUsername(username);
        }
    }

    @Cacheable(value = "dataTransfer")
    @Override
    public DataExample getDataById(String token, Long id) throws InterruptedException {
        Thread.sleep(5000);
        Object value = myMap.get(token);
        if (value == null){
            throw new RuntimeException("Token invalid");
        } else {
            log.info("value not null");
            String username = value.toString();
            DataExample dataExample = dataExampleRepository.findByUsernameAndId(username, id);
            dataExample.setId(id);
            dataMap.put(dataExample.getId(), dataExample);
            return dataExample;
        }
    }
}
