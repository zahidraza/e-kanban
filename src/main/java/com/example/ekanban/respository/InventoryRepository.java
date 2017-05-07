/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ekanban.respository;

import com.example.ekanban.entity.Inventory;
import com.example.ekanban.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 *
 * @author razamd
 */
public interface InventoryRepository extends JpaRepository<Inventory, Long>{

    Inventory findByProductAndBinNo(Product product,Integer binNo);

    List<Inventory> findByLastUpdatedGreaterThan(Date date);
}
