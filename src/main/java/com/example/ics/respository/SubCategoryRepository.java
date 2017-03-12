/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ics.respository;

import com.example.ics.entity.Category;
import com.example.ics.entity.SubCategory;
import java.io.Serializable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author razamd
 */
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long>{
    
    public SubCategory findByName(String name);
    
    public Page<SubCategory> findByCategory(Category category, Pageable pageable);
}
