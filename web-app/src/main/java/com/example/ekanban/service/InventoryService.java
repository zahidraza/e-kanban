/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ekanban.service;

import com.example.ekanban.dto.InventoryDto;
import com.example.ekanban.entity.Consumption;
import com.example.ekanban.entity.Inventory;
import com.example.ekanban.entity.Product;
import com.example.ekanban.enums.BinState;
import com.example.ekanban.exception.ScanException;
import com.example.ekanban.respository.ConsumptionRepository;
import com.example.ekanban.respository.InventoryRepository;
import com.example.ekanban.respository.ProductRepository;
import com.example.ekanban.storage.BarcodeService;
import com.example.ekanban.util.MiscUtil;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired private ProductRepository productRepository;
    @Autowired private BarcodeService barcodeService;
    @Autowired private ConsumptionRepository consumptionRepository;

    private final Mapper mapper;

    public InventoryService(InventoryRepository inventoryRepository, Mapper mapper) {
        this.inventoryRepository = inventoryRepository;
        this.mapper = mapper;
    }

    @Transactional
    public InventoryDto update(InventoryDto inventory){
        Inventory inventory2 = inventoryRepository.findOne(inventory.getId());
        if (inventory2.getBinState() == BinState.FREEZED) {
            throw new ScanException("Outward scan done but no pending order generated as it was surplus bin.");
        }
        //If outward Scan is being done
        if (inventory.getBinState().equalsIgnoreCase(BinState.PURCHASE.getValue())) {
            Product product = productRepository.findOne(inventory2.getProduct().getId());
            List<Inventory> stockList = inventoryRepository.findByProductAndBinState(product,BinState.STORE.getValue());
            int binsInStock = stockList.size();
            long binQty = product.getBinQty();
            product.setStkOnFloor(binQty*(binsInStock-1));
            Consumption consumption = consumptionRepository.findByProductAndYearAndMonth(product, MiscUtil.getCurrentYear(),MiscUtil.getCurrentMonth());
            consumption.setValue(consumption.getValue()+binQty);
        }
        inventory2.setBinState(BinState.parse(inventory.getBinState()));
        return mapper.map(inventory2, InventoryDto.class);
    }

//    /**
//     * check whether pending order should be generated after outward scan
//     * @param product
//     * @return
//     */
//    public boolean isOutwardScanPossible(Product product) {
//        int binsInStock = inventoryRepository.findByProduct(product).stream()
//                .filter(inv -> inv.getBinState() == BinState.STORE)
//                .collect(Collectors.toList()).size();
//        if (binsInStock <= product.getNoOfBins()) {
//            return true;
//        }
//        return false;
//    }

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
