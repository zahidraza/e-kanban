package com.example.ekanban.restcontroller;

import com.example.ekanban.assembler.SupplierAssembler;
import com.example.ekanban.dto.SupplierDto;
import com.example.ekanban.service.SupplierService;
import com.example.ekanban.util.ApiUrls;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.http.HttpHeaders;
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
@RequestMapping(ApiUrls.ROOT_URL_SUPPLIERS)
public class SupplierRestController { 
    private final Logger logger = LoggerFactory.getLogger(SupplierRestController.class);
    
    private final SupplierService supplierService;
    private final SupplierAssembler supplierAssembler;

    @Autowired
    public SupplierRestController(SupplierService supplierService, SupplierAssembler supplierAssembler) {
        this.supplierService = supplierService;
        this.supplierAssembler = supplierAssembler;
    }
    
//    @GetMapping
//    public ResponseEntity<?> listAllSuppliers(Pageable pageable, PagedResourcesAssembler assembler) {
//        logger.debug("listAllSuppliers()");
//        Page<Supplier> page = supplierService.findAllByPage(pageable);
//        return new ResponseEntity<>(assembler.toResource(page, supplierAssembler), HttpStatus.OK);
//    }

    @GetMapping
    public ResponseEntity<?> listAllSuppliers() {
        logger.debug("listAllSuppliers()");
        List<SupplierDto> suppliers = supplierService.findAll();
        return new ResponseEntity<>(supplierAssembler.toResources(suppliers), HttpStatus.OK);
    }
    
    @GetMapping(ApiUrls.URL_SUPPLIERS_SUPPLIER)
    public ResponseEntity<?> loadSupplier(@PathVariable("supplierId") Long supplierId){
        logger.debug("loadSupplier(): supplierId = {}",supplierId);
        SupplierDto supplier = supplierService.findOne(supplierId);
        if (supplier == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(supplierAssembler.toResource(supplier), HttpStatus.OK);
    }
    
    
    @PostMapping
    public ResponseEntity<?> createSupplier(@Valid @RequestBody SupplierDto supplierDto) {
        logger.debug("createSupplier()");
        supplierDto = supplierService.save(supplierDto);
        Link selfLink = linkTo(SupplierRestController.class).slash(supplierDto.getId()).withSelfRel();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(selfLink.getHref()));
        return new ResponseEntity<>(supplierAssembler.toResource(supplierDto),headers,HttpStatus.CREATED);
    }
 
    @PutMapping(ApiUrls.URL_SUPPLIERS_SUPPLIER)
    public ResponseEntity<?> updateSupplier(@PathVariable("supplierId") long supplierId,@Valid @RequestBody SupplierDto supplierDto) {
        logger.debug("updateSupplier(): supplierId = {}",supplierId);
        if (!supplierService.exists(supplierId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        supplierDto.setId(supplierId);
        supplierDto = supplierService.update(supplierDto);
        return new ResponseEntity<>(supplierAssembler.toResource(supplierDto), HttpStatus.OK);
    }
  
    @DeleteMapping(ApiUrls.URL_SUPPLIERS_SUPPLIER)
    public ResponseEntity<Void> deleteSupplier(@PathVariable("supplierId") long supplierId) {
        logger.debug("deleteSupplier(): id = {}",supplierId);
        if (!supplierService.exists(supplierId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        supplierService.delete(supplierId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
