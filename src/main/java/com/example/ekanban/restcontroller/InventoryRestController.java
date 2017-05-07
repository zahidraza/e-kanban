package com.example.ekanban.restcontroller;

import com.example.ekanban.assembler.InventoryAssembler;
import com.example.ekanban.dto.InventoryDto;
import com.example.ekanban.service.InventoryService;
import com.example.ekanban.util.ApiUrls;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> getAllInventory(@RequestParam(value = "after", defaultValue = "0") Long after){
        List<InventoryDto> list = inventoryService.findAllAfter(after);
        Map<String,Object> resp = new HashedMap();
        resp.put("invSync", System.currentTimeMillis());
        resp.put("inventory", inventoryAssembler.toResources(list));
        return  ResponseEntity.ok(resp);
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

}
