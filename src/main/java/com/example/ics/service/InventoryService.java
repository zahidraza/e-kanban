/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ics.service;

import com.example.ics.entity.Inventory;
import com.example.ics.respository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    private Inventory update(Inventory inventory){
        Inventory inventory2 = inventoryRepository.findOne(inventory.getId());
        inventory2.setBinState(inventory.getBinState());
        return inventory2;
    }

    private List<Inventory> findAll(){
        return null;
    }
}
