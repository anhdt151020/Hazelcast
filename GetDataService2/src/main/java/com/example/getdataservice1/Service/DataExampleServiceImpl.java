package com.example.getdataservice1.Service;

import com.example.getdataservice1.Entity.DataExample;
import com.example.getdataservice1.Entity.DataTransferModel;
import com.example.getdataservice1.Repository.DataExampleRepository;
import com.example.getdataservice1.Repository.DataTransferModelRepository;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataExampleServiceImpl implements DataExampleService{

    private final DataExampleRepository dataExampleRepository;
    private final DataTransferModelRepository dataTransferModelRepository;
    HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();

    IMap<String, Object> authMap = hazelcastInstance.getMap("auth-map");
    IMap<Long, Object> dataMap = hazelcastInstance.getMap("data-map");
    IMap<Long, Object> transferMap = hazelcastInstance.getMap("transfer-map");

    @Override
    public DataExample saveNewRecord(DataExample dataExample) {
        DataExample record = dataExampleRepository.save(dataExample);
        dataMap.put(Long.parseLong(record.getId().toString()), record);
        return record;
    }

    @Cacheable(value = "dataExample")
    @Override
    public List<DataExample> getDataByToken(String token) throws InterruptedException {
        Thread.sleep(5000);
        List<DataExample> res;
        Object value = authMap.get(token);
        if (value == null){
            throw new RuntimeException("Token invalid");
        } else {
            log.info("value not null");
            String username = value.toString();
            res = dataExampleRepository.findAllByUsername(username);
            res.forEach(o -> {
                dataMap.put(Long.parseLong(o.getId().toString()), o);
            });
        }
        return res;
    }

    @Override
    public DataExample getDataById(String token, String id) throws InterruptedException {
        DataExample res;
        Object value = authMap.get(token);
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

    @Override
    public DataTransferModel getTransferData(HttpServletRequest request, Long id) {
        String token = null;
        final String authorizationHeaderValue = request.getHeader("Authorization");
        if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer")) {
            token = authorizationHeaderValue.substring(7);
        }
        assert token != null;
        Object value = authMap.get(token);
        if (value == null) {
            throw new RuntimeException("Token invalid");
        }
        try {
            DataTransferModel dataTransferModel = (DataTransferModel) transferMap.get(id);
            return dataTransferModelRepository.save(dataTransferModel);
        } catch (Exception e) {
            DataExample data = (DataExample) dataMap.get(id);
            DataTransferModel res = DataTransferModel.builder()
                    .id(data.getId())
                    .Data(data.getData())
                    .authType(request.getAuthType())
                    .method(request.getMethod())
                    .requestURI(request.getRequestURI())
                    .sessionId(request.getRequestedSessionId())
                    .username(value.toString())
                    .build();
            transferMap.put(id, res);
            return dataTransferModelRepository.save(res);
        }
    }

    @Override
    public DataTransferModel updateTransferData(DataTransferModel data, Long id) {
        DataTransferModel res;
        try {
            res = (DataTransferModel) transferMap.get(id);
            if (res != null) {
                res = DataTransferModel.builder()
                        .id(data.getId())
                        .Data(data.getData())
                        .authType(data.getAuthType())
                        .method(data.getMethod())
                        .requestURI(data.getRequestURI())
                        .sessionId(data.getSessionId())
                        .username(data.getUsername())
                        .build();
                transferMap.put(id, res);
            }
        } catch (NullPointerException e) {
            return null;
        }
        return res;
    }

    public DataExample getDataFromHazelcast(String id){
        return (DataExample) dataMap.get(Long.parseLong(id));
    }
}
