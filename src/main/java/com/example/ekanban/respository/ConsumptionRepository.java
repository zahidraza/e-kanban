/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ekanban.respository;

import com.example.ekanban.entity.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author razamd
 */
public interface ConsumptionRepository extends JpaRepository<Consumption, Long>{
    
}
