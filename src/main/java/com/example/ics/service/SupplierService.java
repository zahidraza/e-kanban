package com.example.ics.service;


import com.example.ics.entity.Supplier;
import com.example.ics.respository.SupplierRepository;
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
public class SupplierService {
    private final Logger logger = LoggerFactory.getLogger(SupplierService.class);

    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public Supplier findOne(Long id) {
        logger.debug("findOne(): id = {}",id);
        return supplierRepository.findOne(id);
    }

    public List<Supplier> findAll() {
        logger.debug("findAll()");
        return supplierRepository.findAll();
    }
    
    public Page<Supplier> findAllByPage(Pageable pageable){
        logger.debug("findAllByPage()");
        return supplierRepository.findAll(pageable);
    }
    
    public Supplier findByName(String name) {
        logger.debug("findByName(): name = " , name);
        return supplierRepository.findByName(name);
    }

    public Boolean exists(Long id) {
        logger.debug("exists(): id = ",id);
        return supplierRepository.exists(id);
    }
    
    public Long count(){
        logger.debug("count()");
        return supplierRepository.count();
    }

    @Transactional
    public Supplier save(Supplier supplier) {
        logger.debug("save()");
        supplier = supplierRepository.save(supplier);
        return supplier;
    }

//    @Transactional
//    public Supplier update(Supplier supplier) {
//        logger.debug("update()");
//        Supplier supplier2 = supplierRepository.findOne(supplier.getId());
//        supplier2.setName(supplier.getName());
//        return supplier2;
//    }
    
    @Transactional
    public void delete(Long id) {
        logger.debug("delete(): id = {}",id);
        supplierRepository.delete(id);
    }
}
