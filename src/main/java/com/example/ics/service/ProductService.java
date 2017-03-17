/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ics.service;

import com.example.ics.dto.ProductDto;
import com.example.ics.entity.Category;
import com.example.ics.entity.Product;
import com.example.ics.entity.SubCategory;
import com.example.ics.page.converter.ProductConverter;
import com.example.ics.respository.ProductRepository;
import java.util.List;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);
    
    private final ProductRepository productRepository;
    private ProductConverter converter;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Autowired
    public void setConverter(ProductConverter converter) {
        this.converter = converter;
    }
    
    public Product findOne(Long id, boolean initSections, boolean initSuupliers){
        logger.debug("findOne(): id = {}",id);
        Product product = productRepository.findOne(id);
        if(product != null && initSections){
            Hibernate.initialize(product.getSectionList());
        }
        if(product != null && initSuupliers){
            Hibernate.initialize(product.getSupplierList());
        }
        return productRepository.findOne(id);
    }
    
    public List<Product> findAll() {
        logger.debug("findAll()");
        return productRepository.findAll();
    }
    
    public Page<ProductDto> findPageBySubCategory(SubCategory subCategory, Pageable pageable){
        logger.debug("findAllByPage()");
        return productRepository.findBySubCategory(subCategory,pageable).map(converter);
    }

    
    public Product findByName(String name) {
        logger.debug("findByName(): name = " , name);
        return productRepository.findByName(name);
    }

    public Boolean exists(Long id) {
        logger.debug("exists(): id = ",id);
        return productRepository.exists(id);
    }
    
    public Long count(){
        logger.debug("count()");
        return productRepository.count();
    }

    @Transactional
    public Product save(Product product) {
        logger.debug("save()");
        return productRepository.save(product);
    }

//    @Transactional
//    public Product update(Product product) {
//        logger.debug("update()");
//        Product product2 = productRepository.findOne(product.getId());
//        product2.setName(product.getName());
//        return product2;
//    }
    
    @Transactional
    public void delete(Long id) {
        logger.debug("delete(): id = {}",id);
        productRepository.delete(id);
    }
    
}
