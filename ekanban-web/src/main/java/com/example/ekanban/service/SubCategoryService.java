package com.example.ekanban.service;

import com.example.ekanban.entity.Category;
import com.example.ekanban.entity.SubCategory;
import com.example.ekanban.respository.SubCategoryRepository;
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
public class SubCategoryService {    
    private final Logger logger = LoggerFactory.getLogger(SubCategoryService.class);
    
    private final SubCategoryRepository subCategoryRepository;
    
    @Autowired
    public SubCategoryService(SubCategoryRepository subCategoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
    }
    
    public SubCategory findOne(Long id){
        logger.debug("findOne(): id = {}",id);
        return subCategoryRepository.findOne(id);
    }
    
    public List<SubCategory> findAll() {
        logger.debug("findAll()");
        return subCategoryRepository.findAll();
    }
    
    public Page<SubCategory> findPageAll(Pageable pageable){
        logger.debug("findAllByPage()");
        return subCategoryRepository.findAll(pageable);
    }
    
    public Page<SubCategory> findPageByCategory(Category category,Pageable pageable){
        logger.debug("findPageByCategory()");
        return subCategoryRepository.findByCategory(category,pageable);
    }

    
    public SubCategory findByName(String name) {
        logger.debug("findByNameIgnoreCase(): name = " , name);
        return subCategoryRepository.findByNameIgnoreCase(name);
    }

    public Boolean exists(Long id) {
        logger.debug("exists(): id = ",id);
        return subCategoryRepository.exists(id);
    }
    
    public Long count(){
        logger.debug("count()");
        return subCategoryRepository.count();
    }

    @Transactional
    public SubCategory save(SubCategory subCategory) {
        logger.debug("save()");
        return subCategoryRepository.save(subCategory);
    }

    @Transactional
    public SubCategory update(SubCategory subCategory) {
        logger.debug("update()");
        SubCategory subCategory2 = subCategoryRepository.findOne(subCategory.getId());
        subCategory2.setName(subCategory.getName());
        return subCategory2;
    }
    
    @Transactional
    public void delete(Long id) {
        logger.debug("delete(): id = {}",id);
        subCategoryRepository.delete(id);
    }
    
}
