package com.example.getdataservice1.Service;

import com.example.getdataservice1.Entity.DataExample;
import com.example.getdataservice1.Repository.DataExampleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataExampleServiceImpl implements DataExampleService{

    private final DataExampleRepository dataExampleRepository;
    private final RedisTemplate<Object, Object> template;
    @Override
    public DataExample saveNewRecord(DataExample dataExample) {
        return dataExampleRepository.save(dataExample);
    }

    @Cacheable(value = "dataExample")
    @Override
    public List<DataExample> getDataByToken(String token) throws InterruptedException {
        Thread.sleep(5000);
        Object value = template.opsForValue().get(token);
        if (value == null){
            throw new RuntimeException("Token invalid");
        } else {
            log.info("value not null");
            String username = value.toString();
            return dataExampleRepository.findAllByUsername(username);
        }
    }
}
