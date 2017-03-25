package com.example.ics.service;

import com.example.ics.entity.Category;
import com.example.ics.respository.CategoryRepository;
import java.util.List;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CategoryService {
    private final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    
    private final CategoryRepository categoryRepository;
    
    @Autowired 
    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }
    
    public Category findOne(Long id, boolean initSubCategory, boolean initProduct){
        logger.debug("findOne(): id = {}",id);
        Category category = categoryRepository.findOne(id);
        if(category != null && initSubCategory && !initProduct){
            Hibernate.initialize(category.getSubCategoryList());
        }
        if(category != null && initSubCategory && initProduct){
            Hibernate.initialize(category.getSubCategoryList());
            category.getSubCategoryList()
                    .forEach(s -> Hibernate.initialize(s.getProductList()));
        }
        return category;
    }
    
    public List<Category> findAll() {
        logger.debug("findAll()");
        return categoryRepository.findAll();
    }
    
    public Page<Category> findAllByPage(Pageable pageable,boolean expand){
        logger.debug("findAllByPage()");
        Page<Category> page = categoryRepository.findAll(pageable);
        if(!expand)  page.forEach(category -> category.setSubCategoryList(null));
        return page;
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
