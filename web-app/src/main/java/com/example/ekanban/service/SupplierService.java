package com.example.ekanban.service;


import com.example.ekanban.dto.SupplierDto;
import com.example.ekanban.entity.Address;
import com.example.ekanban.entity.Supplier;
import com.example.ekanban.respository.SupplierRepository;
import java.util.List;
import java.util.stream.Collectors;

import org.dozer.Mapper;
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

    @Autowired Mapper mapper;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public SupplierDto findOne(Long id) {
        logger.debug("findOne(): id = {}",id);
        Supplier supplier = supplierRepository.findOne(id);
        if (supplier != null) {
            return mapper.map(supplier, SupplierDto.class);
        }
        return null;
    }

    public List<SupplierDto> findAll() {
        logger.debug("findAll()");
        return supplierRepository.findAll().stream()
                .map(supplier -> mapper.map(supplier,SupplierDto.class))
                .collect(Collectors.toList());
    }
//
//    public Page<Supplier> findAllByPage(Pageable pageable){
//        logger.debug("findAllByPage()");
//        return supplierRepository.findAll(pageable);
//    }
    
    public SupplierDto findByName(String name) {
        logger.debug("findByNameIgnoreCase(): name = " , name);
        Supplier supplier = supplierRepository.findByNameIgnoreCase(name);
        if (supplier != null) {
            return mapper.map(supplier, SupplierDto.class);
        }
        return null;
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
    public SupplierDto save(SupplierDto supplierDto) {
        logger.debug("save()");
        Supplier supplier = mapper.map(supplierDto,Supplier.class);
        supplier = supplierRepository.save(supplier);
        return mapper.map(supplier,SupplierDto.class);
    }

    @Transactional
    public SupplierDto update(SupplierDto supplierDto) {
        logger.debug("update()");
        Supplier supplier = supplierRepository.findOne(supplierDto.getId());

        if(supplierDto.getName() != null)  supplier.setName(supplierDto.getName());
        if(supplierDto.getContactPerson() != null)  supplier.setContactPerson(supplierDto.getContactPerson());
        if(supplierDto.getSupplierType() != null)  supplier.setSupplierType(supplierDto.getSupplierType());
        if(supplierDto.getAddress() != null){
            Address address = supplierDto.getAddress();
            Address address2 = (supplier.getAddress() != null) ? supplier.getAddress() : new Address();
            if(address.getStreet() != null) address2.setStreet(address.getStreet());
            if(address.getLandmark() != null) address2.setLandmark(address.getLandmark());
            if(address.getCity() != null) address2.setCity(address.getCity());
            if(address.getState() != null) address2.setState(address.getState());
            if(address.getCountry() != null) address2.setCountry(address.getCountry());
            if(address.getZip() != null) address2.setZip(address.getZip());
            supplier.setAddress(address2);
        }
        return mapper.map(supplier,SupplierDto.class);
    }
    
    @Transactional
    public void delete(Long id) {
        logger.debug("delete(): id = {}",id);
        supplierRepository.delete(id);
    }
}
