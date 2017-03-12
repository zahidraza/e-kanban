package com.example.ics.service;

import com.example.ics.entity.Category;
import com.example.ics.respository.CategoryRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CategoryService {
    
    private final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    
    @Autowired CategoryRepository categoryRepository;
    
    public Category findOne(Long id){
        logger.debug("findOne(): id = {}",id);
        return categoryRepository.findOne(id);
    }
    
    public List<Category> findAll() {
        logger.debug("findAll()");
        return categoryRepository.findAll();
    }
    
    public Page<Category> findAllByPage(Pageable pageable){
        logger.debug("findAllByPage()");
        return categoryRepository.findAll(pageable);
    }

    
    public Category findByName(String name) {
        logger.debug("findByName(): name = " , name);
        return categoryRepository.findByName(name);
    }

    public Boolean exists(Long id) {
        logger.debug("exists(): id = ",id);
        return categoryRepository.exists(id);
    }
    
    public Long count(){
        logger.debug("count()");
        return categoryRepository.count();
    }

    @Transactional
    public Category save(Category category) {
        logger.debug("save()");
        return categoryRepository.save(category);
    }

    @Transactional
    public Category update(Category category) {
        logger.debug("update()");
        Category category2 = categoryRepository.findOne(category.getId());
        category2.setName(category.getName());
        return category2;
    }
    
    @Transactional
    public void delete(Long id) {
        logger.debug("delete(): id = {}",id);
        categoryRepository.delete(id);
    }
    
}
