/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ekanban.respository;

import com.example.ekanban.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author razamd
 */
public interface SectionRepository extends JpaRepository<Section, Long>{
    public Section findByName(String name);
}
