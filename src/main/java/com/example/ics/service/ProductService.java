/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ics.service;

import com.example.ics.dto.ProductCsv;
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

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.ics.util.CsvUtils;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import org.dozer.Mapper;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);
    
    private final ProductRepository productRepository;
    private final SectionRepository sectionRepository;
    private final SupplierRepository supplierRepository;
    private ProductConverter converter;
    private Mapper mapper;

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

    @Autowired
    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    public ProductDto findOne(Long id, boolean initSections, boolean initSuupliers){
        logger.debug("findOne(): id = {}",id);
        Product product = productRepository.findOne(id);
        if(product != null && initSections){
            Hibernate.initialize(product.getSectionList());
        }
        if(product != null && initSuupliers){
            Hibernate.initialize(product.getSupplierList());
        }
        return mapper.map(product,ProductDto.class);
    }
    
    public List<ProductDto> findAll() {
        logger.debug("findAll()");
        return productRepository
            .findAll().stream()
                 .map(product -> mapper.map(product,ProductDto.class))
                 .collect(Collectors.toList());
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

    
    public ProductDto findByName(String name) {
        logger.debug("findByName(): name = " , name);
        return mapper.map(productRepository.findByName(name),ProductDto.class);
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
    public ProductDto save(Product product, List<Long> sections, List<Long> suppliers ) {
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
        return mapper.map(product, ProductDto.class);
    }

    @Transactional
    public ProductDto update(Product product, List<Long> sections, List<Long> suppliers ) {
        logger.debug("update()");
        Product product2 = productRepository.findOne(product.getId());

        map(product,product2);

        Set<Section> sectionList = product2.getSectionList();
        sectionList.clear();
        sections.forEach(secId -> {
            sectionList.add(sectionRepository.findOne(secId));
        });
        Set<Supplier> supplierList = product2.getSupplierList();
        supplierList.clear();
        suppliers.forEach(secId -> {
            supplierList.add(supplierRepository.findOne(secId));
        });
        return mapper.map(product2, ProductDto.class);
    }
    
    @Transactional
    public void delete(Long id) {
        logger.debug("delete(): id = {}",id);
        productRepository.delete(id);
    }

    public void addProductsBatch(MultipartFile file) {
        try {
            //convert to csv string
            String output = null;
            if (file.getName().contains("xlsx")) {
                output = CsvUtils.fromXlsx(file.getInputStream());
            } else if (file.getName().contains("xls")) {
                output = CsvUtils.fromXls(file.getInputStream());
            }

            //Read as Bean from csv String
            CSVReader reader = new CSVReader(new StringReader(output));
            HeaderColumnNameMappingStrategy<ProductCsv> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(ProductCsv.class);
            CsvToBean<ProductCsv> csvToBean = new CsvToBean<>();
            List<ProductCsv> list = csvToBean.parse(strategy, reader);

            //validate value


            //convert from DTO to ENTITY


        }catch (Exception e){

        }
    }

    //src object is latest value, dest object is old value
    private void map(Product src, Product dest) {
        if(src.getName() != null) dest.setName(src.getName());
        if(src.getDescription() != null) dest.setDescription(src.getDescription());
        if(src.getPrice() != null) dest.setPrice(src.getPrice());
        if(src.getItemCode() != null) dest.setItemCode(src.getItemCode());
        if(src.getTimeOrdering() != null) dest.setTimeOrdering(src.getTimeOrdering());
        if(src.getTimeProcurement() != null) dest.setTimeProcurement(src.getTimeProcurement());
        if(src.getTimeTransporation() != null) dest.setTimeTransporation(src.getTimeTransporation());
        if(src.getTimeBuffer() != null) dest.setTimeBuffer(src.getTimeBuffer());
        if(src.getUomPurchase() != null) dest.setUomPurchase(src.getUomPurchase());
        if(src.getUomConsumption() != null) dest.setUomConsumption(src.getUomConsumption());
        if(src.getConversionFactor() != null) dest.setConversionFactor(src.getConversionFactor());
        if(src.getMinOrderQty() != null) dest.setMinOrderQty(src.getMinOrderQty());
        if(src.getPacketSize() != null) dest.setPacketSize(src.getPacketSize());
        if(src.getClassType() != null) dest.setClassType(src.getClassType());
        if(src.getKanbanType() != null) dest.setKanbanType(src.getKanbanType());
        if(src.getBinQty() != null) dest.setBinQty(src.getBinQty());
        if(src.getNoOfBins() != null) dest.setNoOfBins(src.getNoOfBins());
        if(src.getDemand() != null) dest.setDemand(src.getDemand());
    }
    
}
