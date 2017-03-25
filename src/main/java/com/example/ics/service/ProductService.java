/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ics.service;

import com.example.ics.dto.ProductDto;
import com.example.ics.entity.Category;
import com.example.ics.entity.Product;
import com.example.ics.entity.Section;
import com.example.ics.entity.SubCategory;
import com.example.ics.entity.Supplier;
import com.example.ics.page.converter.ProductConverter;
import com.example.ics.respository.ProductRepository;
import com.example.ics.respository.SectionRepository;
import com.example.ics.respository.SupplierRepository;
import java.util.List;
import java.util.Set;
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
    private final SectionRepository sectionRepository;
    private final SupplierRepository supplierRepository;
    private ProductConverter converter;

    @Autowired
    public ProductService(ProductRepository productRepository,SectionRepository sectionRepository,SupplierRepository supplierRepository) {
        this.productRepository = productRepository;
        this.sectionRepository = sectionRepository;
        this.supplierRepository = supplierRepository;
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
        return product;
    }
    
    public List<Product> findAll() {
        logger.debug("findAll()");
        return productRepository.findAll();
    }
    
    public Page<ProductDto> findPageBySubCategory(SubCategory subCategory, Pageable pageable){
        logger.debug("findPageBySubCategory()");
        Page<Product> page = productRepository.findBySubCategory(subCategory,pageable);
        page.forEach(p -> {
            Hibernate.initialize(p.getSectionList());
            Hibernate.initialize(p.getSupplierList());
        });
        return page.map(converter);
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
    public Product save(Product product, List<Long> sections, List<Long> suppliers ) {
        logger.debug("save()");
        product = productRepository.save(product);
        Set<Section> sectionList = product.getSectionList();
        Set<Supplier> supplierList = product.getSupplierList();
        if(sections != null){
            sections.forEach(secId -> {
                sectionList.add(sectionRepository.findOne(secId));
            });
        }
        if(suppliers != null){
            suppliers.forEach(secId -> {
                supplierList.add(supplierRepository.findOne(secId));
            });
        }
        return product;
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
