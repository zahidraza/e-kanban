package com.example.ics.assembler;

import com.example.ics.entity.Supplier;
import com.example.ics.restcontroller.SupplierRestController;
import org.springframework.hateoas.Resource;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class SupplierAssembler extends ResourceAssemblerSupport<Supplier, Resource>{

    public SupplierAssembler() {
        super(SupplierRestController.class, Resource.class);
    }

    @Override
    public Resource toResource(Supplier supplier) {       
        return new Resource<>(supplier, linkTo(SupplierRestController.class).slash(supplier.getId()).withSelfRel()); 
    }
    
}
