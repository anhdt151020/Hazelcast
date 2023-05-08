package com.example.getdataservice1.Entity;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SavingFailedDataEvent extends ApplicationEvent {
    DataMakerModel dataMakerModel;
    public SavingFailedDataEvent(Object source, DataMakerModel dataMakerModel) {
        super(source);
        this.dataMakerModel = dataMakerModel;
    }
}
