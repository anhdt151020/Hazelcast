package com.example.getdataservice1.Controller;

import com.example.getdataservice1.Entity.DataExample;
import com.example.getdataservice1.Repository.DataExampleRepository;
import com.example.getdataservice1.Service.DataExampleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/get-data2")
@RequiredArgsConstructor
public class DataController {

    private final DataExampleService dataExampleService;

    @PostMapping("/add-data")
    public DataExample addData(@RequestBody DataExample dataExample){
        return dataExampleService.saveNewRecord(dataExample);
    }

    @GetMapping("/getData")
    public ResponseEntity<List<DataExample>> getData(HttpServletRequest request) throws InterruptedException {
        String token = null;
        final String authorizationHeaderValue = request.getHeader("Authorization");
        if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer")) {
            token = authorizationHeaderValue.substring(7);
        }
        return ResponseEntity.ok().body(dataExampleService.getDataByToken(token));
    }
}
