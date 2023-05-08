package com.example.getdataservice1.Service;

import com.example.getdataservice1.Entity.DataExample;
import com.example.getdataservice1.Entity.DataTransferModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface DataExampleService {
    DataExample saveNewRecord(DataExample dataExample);

    List<DataExample> getDataByToken(String token) throws InterruptedException;

    DataExample getDataById(String token, String id) throws InterruptedException;

    DataTransferModel getTransferData(HttpServletRequest request, Long id);

    DataTransferModel updateTransferData(DataTransferModel dataTransferModel, Long id);
}
