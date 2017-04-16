package com.example.ics.restcontroller;

import com.example.ics.assembler.InventoryAssembler;
import com.example.ics.dto.InventoryDto;
import com.example.ics.service.InventoryService;
import com.example.ics.util.ApiUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
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
}
