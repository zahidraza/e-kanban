package com.example.ekanban.restcontroller;

import com.example.ekanban.assembler.InventoryAssembler;
import com.example.ekanban.dto.InventoryDto;
import com.example.ekanban.service.InventoryService;
import com.example.ekanban.util.ApiUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by razamd on 4/16/2017.
 */
@RestController
@RequestMapping(ApiUrls.ROOT_URL_INVENTORY)
public class InventoryRestController {

    @Autowired InventoryAssembler inventoryAssembler;

    @Autowired InventoryService inventoryService;

    @GetMapping(ApiUrls.URL_INVENTORY_SINGLE)
    public ResponseEntity<?> getInventory(@PathVariable("id") long id){
        if (!inventoryService.exists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(inventoryAssembler.toResource(inventoryService.findOne(id)));
    }

    @GetMapping
    public ResponseEntity<?> getAllInventory(){
        List<InventoryDto> list = inventoryService.findAll();
        return  ResponseEntity.ok(inventoryAssembler.toResources(list));
    }

    @PatchMapping(ApiUrls.URL_INVENTORY_SINGLE)
    public ResponseEntity<?> updateInventory(@PathVariable("id") Long id, @Valid @RequestBody InventoryDto inventoryDto){
        if (!inventoryService.exists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        inventoryDto.setId(id);
        inventoryDto = inventoryService.update(inventoryDto);
        return ResponseEntity.ok(inventoryAssembler.toResource(inventoryDto));
    }

    @PostMapping(ApiUrls.URL_INVENTORY_SINGLE)
    public ResponseEntity<org.springframework.core.io.Resource> printBarcode(@PathVariable("id") long id){
        if (!inventoryService.exists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        org.springframework.core.io.Resource file = inventoryService.printBarcode(id);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\"" + file.getFilename() + "\"");
//        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .body(file);

    }
}
