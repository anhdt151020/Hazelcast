package com.example.getdataservice1.Service;

import com.example.getdataservice1.Entity.DataExample;
import com.example.getdataservice1.Repository.DataExampleRepository;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataExampleServiceImpl implements DataExampleService{

    private final DataExampleRepository dataExampleRepository;
    private final RedisTemplate<Object, Object> template;
    HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();

    Map<String, Object> dataExampleMap = hazelcastInstance.getMap("example-map");
    @Override
    public DataExample saveNewRecord(DataExample dataExample) {
        DataExample record = dataExampleRepository.save(dataExample);
        dataExampleMap.put(record.getId().toString(), record);
        return record;
    }

    @Cacheable(value = "dataExample")
    @Override
    public List<DataExample> getDataByToken(String token) throws InterruptedException {
        Thread.sleep(5000);
        List<DataExample> res;
        Object value = template.opsForValue().get(token);
        if (value == null){
            throw new RuntimeException("Token invalid");
        } else {
            log.info("value not null");
            String username = value.toString();
            res = dataExampleRepository.findAllByUsername(username);
            res.forEach(o -> {
                dataExampleMap.put(o.getId().toString(), o);
            });
        }
        return res;
    }

    @Override
    public DataExample getDataById(String token, String id) throws InterruptedException {
        DataExample res;
        Object value = template.opsForValue().get(token);
        if (value == null) {
            throw new RuntimeException("Token invalid");
        } else {
            res = getDataFromHazelcast(id);
            if (res == null) {
                Thread.sleep(5000);
                res = dataExampleRepository.getReferenceById(Long.parseLong(id));
                if (!Objects.equals(value.toString(), res.getUsername())){
                    throw new RuntimeException("Permission denied!");
                }
            }
            return res;
        }
    }
    public DataExample getDataFromHazelcast(String id){
        return (DataExample) dataExampleMap.get(id);
    }
}
