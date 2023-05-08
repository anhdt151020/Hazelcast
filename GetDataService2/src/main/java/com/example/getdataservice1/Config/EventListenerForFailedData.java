package com.example.getdataservice1.Config;

import com.example.getdataservice1.Entity.DataMakerModel;
import com.example.getdataservice1.Entity.SavingFailedDataEvent;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.context.event.EventListener;

public class EventListenerForFailedData {
    HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
    IMap<String, String> eventMap = hazelcastInstance.getMap("event-map");

    IMap<String, Object> dataExampleMap = hazelcastInstance.getMap("data-map");

    @EventListener
    public void savingFailedDataEvent(SavingFailedDataEvent savingFailedDataEvent){

    }
}
