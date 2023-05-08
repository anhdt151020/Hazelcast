package com.example.getdataservice1.Entity;

import lombok.Data;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MakeDataEvent extends ApplicationEvent {
    DataMakerModel dataMakerModel;
    public MakeDataEvent(Object source, DataMakerModel dataMakerModel) {
        super(source);
        this.dataMakerModel = dataMakerModel;
    }



}
