package com.example.ics.restcontroller;

import com.example.ics.assembler.SupplierAssembler;
import com.example.ics.entity.Supplier;
import com.example.ics.service.SupplierService;
import com.example.ics.util.ApiUrls;
import java.net.URI;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = ApiUrls.ROOT_URL_SUPPLIERS)
public class SupplierRestController { 
    private final Logger logger = LoggerFactory.getLogger(SupplierRestController.class);
    
    private final SupplierService supplierService;
    private final SupplierAssembler supplierAssembler;

    @Autowired
    public SupplierRestController(SupplierService supplierService, SupplierAssembler supplierAssembler) {
        this.supplierService = supplierService;
        this.supplierAssembler = supplierAssembler;
    }
    
    @GetMapping
    public ResponseEntity<?> listAllSuppliers(Pageable pageable, PagedResourcesAssembler assembler) {
        logger.debug("listAllSuppliers()");
        Page<Supplier> page = supplierService.findAllByPage(pageable);
        return new ResponseEntity<>(assembler.toResource(page, supplierAssembler), HttpStatus.OK);
    }
    
    @GetMapping(value = ApiUrls.URL_SUPPLIERS_SUPPLIER)
    public ResponseEntity<?> loadSupplier(@PathVariable("supplierId") Long supplierId){
        logger.debug("loadSupplier(): supplierId = {}",supplierId);
        Supplier supplier = supplierService.findOne(supplierId);
        if (supplier == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(supplierAssembler.toResource(supplier), HttpStatus.OK);
    }
    
    
    @PostMapping
    public ResponseEntity<Void> createSupplier(@Valid @RequestBody Supplier supplier) {
        logger.debug("createSupplier():\n {}", supplier.toString());
        supplier = supplierService.save(supplier);
        Link selfLink = linkTo(SupplierRestController.class).slash(supplier.getId()).withSelfRel();
        return ResponseEntity.created(URI.create(selfLink.getHref())).build();
    }
 
//    @PutMapping(value = ApiUrls.URL_SECTIONS_SECTION)
//    public ResponseEntity<?> updateSupplier(@PathVariable("supplierId") long supplierId,@Valid @RequestBody Supplier supplier) {
//        logger.debug("updateSupplier(): supplierId = {} \n {}",supplierId,supplier);
//        if (!supplierService.exists(supplierId)) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        supplier.setId(supplierId);  
//        supplier = supplierService.update(supplier);
//        return new ResponseEntity<>(supplierAssembler.toResource(supplier), HttpStatus.OK);
//    }
  
    @DeleteMapping(value = ApiUrls.URL_SUPPLIERS_SUPPLIER)
    public ResponseEntity<Void> deleteSupplier(@PathVariable("supplierId") long supplierId) {
        logger.debug("deleteSupplier(): id = {}",supplierId);
        if (!supplierService.exists(supplierId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        supplierService.delete(supplierId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
