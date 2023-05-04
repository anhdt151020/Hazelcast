package com.example.getdataservice1.Controller;

import com.example.getdataservice1.Entity.TransferModel;
import com.example.getdataservice1.Service.TransferDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/get-data/")
@RequiredArgsConstructor
public class TransferDataController {

    private final TransferDataService transferDataService;

    @PostMapping("/transfer2-put")
    public TransferModel put(@RequestBody TransferModel transferModel){
        return transferDataService.putNewData(transferModel);
    }

    @PostMapping("/transfer2-put-and-save")
    public TransferModel putAndSave(@RequestBody TransferModel transferModel){
        return transferDataService.putAndSaveNewData(transferModel);
    }

    @PostMapping("/transfer2-update")
    public TransferModel update(@RequestBody TransferModel transferModel){
        return transferDataService.update(transferModel);
    }

    @GetMapping("/transfer2-get/{key}")
    public TransferModel update(@PathVariable String key) throws InterruptedException {
        return transferDataService.getData(key);
    }

    @PostMapping("transfer2-save/{key}")
    public TransferModel save(@PathVariable String key) {
        return transferDataService.saveKey(key);
    }

}
