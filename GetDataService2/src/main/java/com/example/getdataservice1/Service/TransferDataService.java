package com.example.getdataservice1.Service;

import com.example.getdataservice1.Entity.TransferModel;

public interface TransferDataService {
    TransferModel putNewData(TransferModel transferModel);

    TransferModel putAndSaveNewData(TransferModel transferModel);

    TransferModel update(TransferModel transferModel);

    TransferModel getData(String key) throws InterruptedException;

    TransferModel saveKey(String key);
}
