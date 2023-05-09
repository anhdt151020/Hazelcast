package com.example.getdataservice1.Controller;

import com.example.getdataservice1.Entity.DataExample;
import com.example.getdataservice1.Entity.DataMakerModel;
import com.example.getdataservice1.Entity.TransferModel;
import com.example.getdataservice1.Service.TransferDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/get-data/")
@RequiredArgsConstructor
public class TransferDataController {

    private final TransferDataService transferDataService;

    @PostMapping("/transfer-put")
    public TransferModel put(@RequestBody TransferModel transferModel){
        return transferDataService.putNewData(transferModel);
    }

    @PostMapping("/transfer-put-and-save")
    public TransferModel putAndSave(@RequestBody TransferModel transferModel){
        return transferDataService.putAndSaveNewData(transferModel);
    }

    @PostMapping("/transfer-update")
    public TransferModel update(@RequestBody TransferModel transferModel){
        return transferDataService.update(transferModel);
    }

    @GetMapping("/transfer-get/{key}")
    public TransferModel update(@PathVariable String key) throws InterruptedException {
        return transferDataService.getData(key);
    }

    @PostMapping("/transfer-save/{key}")
    public TransferModel save(@PathVariable String key) {
        return transferDataService.saveKey(key);
    }

    @PostMapping("/make-data")
    public void makeData(HttpServletRequest request, @RequestBody DataMakerModel dataMakerModel){
        transferDataService.makeData(request, dataMakerModel);
    }
}
