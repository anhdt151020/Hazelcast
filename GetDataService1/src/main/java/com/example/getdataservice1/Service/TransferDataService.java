package com.example.getdataservice1.Service;

import com.example.getdataservice1.Entity.DataMakerModel;
import com.example.getdataservice1.Entity.TransferModel;

import javax.servlet.http.HttpServletRequest;

public interface TransferDataService {
    TransferModel putNewData(TransferModel transferModel);

    TransferModel putAndSaveNewData(TransferModel transferModel);

    TransferModel update(TransferModel transferModel);

    TransferModel getData(String key) throws InterruptedException;

    TransferModel saveKey(String key);

    void makeData(HttpServletRequest request, DataMakerModel dataMakerModel);
}
