package com.example.getdataservice1.Service;

import com.example.getdataservice1.Entity.DataExample;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface DataExampleService {
    DataExample saveNewRecord(DataExample dataExample);

    List<DataExample> getDataByToken(String token) throws InterruptedException;
}
