package com.example.ekanban.service;


import com.example.ekanban.entity.Address;
import com.example.ekanban.entity.Supplier;
import com.example.ekanban.respository.SupplierRepository;
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
        logger.debug("findByNameIgnoreCase(): name = " , name);
        return supplierRepository.findByNameIgnoreCase(name);
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

    @Transactional
    public Supplier update(Supplier supplier) {
        logger.debug("update()");
        Supplier supplier2 = supplierRepository.findOne(supplier.getId());

        if(supplier.getName() != null)  supplier2.setName(supplier.getName());
        if(supplier.getContactPerson() != null)  supplier2.setContactPerson(supplier.getContactPerson());
        if(supplier.getSupplierType() != null)  supplier2.setSupplierType(supplier.getSupplierType());
        if(supplier.getAddress() != null){
            Address address = supplier.getAddress();
            Address address2 = (supplier2.getAddress() != null) ? supplier2.getAddress() : new Address();
            if(address.getStreet() != null) address2.setStreet(address.getStreet());
            if(address.getLandmark() != null) address2.setLandmark(address.getLandmark());
            if(address.getCity() != null) address2.setCity(address.getCity());
            if(address.getState() != null) address2.setState(address.getState());
            if(address.getCountry() != null) address2.setCountry(address.getCountry());
            if(address.getZip() != null) address2.setZip(address.getZip());
            supplier2.setAddress(address2);
        }
        return supplier2;
    }
    
    @Transactional
    public void delete(Long id) {
        logger.debug("delete(): id = {}",id);
        supplierRepository.delete(id);
    }
}
