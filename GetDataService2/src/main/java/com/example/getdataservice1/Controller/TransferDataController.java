package com.example.getdataservice1.Controller;

import com.example.getdataservice1.Entity.DataExample;
import com.example.getdataservice1.Entity.DataMakerModel;
import com.example.getdataservice1.Entity.TransferModel;
import com.example.getdataservice1.Service.TransferDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/get-data2/")
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
    public ResponseEntity<PojoExample> update(HttpServletRequest request, @PathVariable String key) throws InterruptedException {
        return ResponseEntity.ok(transferDataService.getData(request, key));
    }

    @PostMapping("transfer2-save/{key}")
    public TransferModel save(@PathVariable String key) {
        return transferDataService.saveKey(key);
    }

    @GetMapping("/make-data/get")
    public DataMakerModel getMakeData(HttpServletRequest request){
        return transferDataService.getMakedData(request);
    }
    @PutMapping("/make-data/update")
    public DataMakerModel updateMakeData(HttpServletRequest request, @RequestBody DataMakerModel dataMakerModel) {
        return transferDataService.updateMakedData(request, dataMakerModel);
    }

}
