package com.example.ekanban.respository;

import com.example.ekanban.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long>{
    
    public Category findByName(String name);
}
