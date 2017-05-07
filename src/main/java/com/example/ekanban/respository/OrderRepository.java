/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ekanban.respository;

import com.example.ekanban.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 *
 * @author razamd
 */
public interface OrderRepository extends JpaRepository<Order, Long>{

    List<Order> findByOrderState(String state);

    List<Order> findByOrderStateAndLastUpdatedGreaterThan(String state, Date date);
}
