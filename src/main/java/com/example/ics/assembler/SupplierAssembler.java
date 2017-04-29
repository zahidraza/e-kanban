package com.example.ics.assembler;

import com.example.ics.entity.Supplier;
import com.example.ics.restcontroller.SupplierRestController;
import org.springframework.hateoas.Resource;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SupplierAssembler extends ResourceAssemblerSupport<Supplier, Resource>{

    public SupplierAssembler() {
        super(SupplierRestController.class, Resource.class);
    }

    @Override
    public Resource toResource(Supplier supplier) {       
        return new Resource<>(supplier, linkTo(SupplierRestController.class).slash(supplier.getId()).withSelfRel()); 
    }

    @Override
    public List<Resource> toResources(Iterable<? extends Supplier> suppliers) {
        List<Resource> resources = new ArrayList<>();
        for(Supplier supplier : suppliers) {
            resources.add(new Resource<>(supplier, linkTo(methodOn(SupplierRestController.class).loadSupplier(supplier.getId())).withSelfRel()));
        }
        return resources;
    }
}
