package com.example.getdataservice1.Config;

import com.example.getdataservice1.Entity.DataMakerModel;
import com.example.getdataservice1.Repository.DataMakerModelRepository;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryLoadedListener;
import com.hazelcast.map.listener.EntryUpdatedListener;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@Slf4j
@EnableAsync
public class HazelcastEntryListenerConfiguration implements
        EntryAddedListener<String, Object>,
        EntryUpdatedListener<String,Object>,
        EntryLoadedListener<String, Object> {

    @Autowired
    private DataMakerModelRepository dataMakerModelRepository;

    Thread1 thread = Thread.currentThread();

    @Override
    @Async
    public void entryAdded(EntryEvent<String, Object> event) {
        DataMakerModel dataMakerModel = (DataMakerModel) event.getValue();
        log.info("Something new here : {}", dataMakerModel);
        try {
            log.info("waiting for love");
            thread.wait(60000);
            log.info("Save Data for failed Process: {}", dataMakerModel);
            dataMakerModelRepository.save(dataMakerModel);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void entryUpdated(EntryEvent<String, Object> event) {
        log.info("Something updated here : {}", event);
        thread.interrupt();
    }

    @Override
    public void entryLoaded(EntryEvent<String, Object> event) {
        log.info("Something loaded here : {}", event);
    }
}
