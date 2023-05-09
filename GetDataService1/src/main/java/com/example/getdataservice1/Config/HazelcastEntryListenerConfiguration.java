package com.example.getdataservice1.Config;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryLoadedListener;
import com.hazelcast.map.listener.EntryUpdatedListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class HazelcastEntryListenerConfiguration implements
        EntryAddedListener<String, Object>,
        EntryUpdatedListener<String,Object>,
        EntryLoadedListener<String, Object> {


    @Override
    public void entryAdded(EntryEvent<String, Object> event) {
        log.info("Something new here : {}", event);
    }

    @Override
    public void entryUpdated(EntryEvent<String, Object> event) {
        log.info("Something updated here : {}", event);
    }

    @Override
    public void entryLoaded(EntryEvent<String, Object> event) {
        log.info("Something loaded here : {}", event);
    }
}
