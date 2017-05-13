/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ekanban.service;

import com.example.ekanban.dto.InventoryDto;
import com.example.ekanban.entity.Inventory;
import com.example.ekanban.enums.BinState;
import com.example.ekanban.respository.InventoryRepository;
import com.example.ekanban.storage.BarcodeService;
import com.example.ekanban.util.MiscUtil;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class InventoryService {
    private final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    private final InventoryRepository inventoryRepository;

    @Autowired private BarcodeService barcodeService;

    private final Mapper mapper;

    public InventoryService(InventoryRepository inventoryRepository, Mapper mapper) {
        this.inventoryRepository = inventoryRepository;
        this.mapper = mapper;
    }

    @Transactional
    public InventoryDto update(InventoryDto inventory){
        Inventory inventory2 = inventoryRepository.findOne(inventory.getId());
        inventory2.setBinState(BinState.parse(inventory.getBinState()));
        return mapper.map(inventory2, InventoryDto.class);
    }

    public InventoryDto findOne(Long id){
        logger.debug("findOne id = {}", id);
        Inventory inv = inventoryRepository.findOne(id);
        if (inv == null) return null;
        return mapper.map(inv, InventoryDto.class);
    }

    public List<InventoryDto> findAllAfter(Long after){
        logger.debug("findAllAfter");
        List<Inventory> list;
        if (after == 0L){
            list = inventoryRepository.findAll();
        }else {
            list = inventoryRepository.findByLastUpdatedGreaterThan(new Date(after));
        }
        return list.stream().map(inventory -> mapper.map(inventory,InventoryDto.class)).collect(Collectors.toList());
    }

    public boolean exists(Long id){
        logger.debug("exists id = {}", id);
        return inventoryRepository.exists(id);
    }

}
