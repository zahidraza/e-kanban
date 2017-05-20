package com.example.ekanban.assembler;

import com.example.ekanban.dto.SupplierDto;
import com.example.ekanban.entity.Supplier;
import com.example.ekanban.restcontroller.SupplierRestController;
import org.springframework.hateoas.Resource;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SupplierAssembler extends ResourceAssemblerSupport<SupplierDto, Resource>{

    public SupplierAssembler() {
        super(SupplierRestController.class, Resource.class);
    }

    @Override
    public Resource toResource(SupplierDto supplier) {
        return new Resource<>(supplier, linkTo(SupplierRestController.class).slash(supplier.getId()).withSelfRel());
    }

    @Override
    public List<Resource> toResources(Iterable<? extends SupplierDto> suppliers) {
        List<Resource> resources = new ArrayList<>();
        for(SupplierDto supplier : suppliers) {
            resources.add(new Resource<>(supplier, linkTo(methodOn(SupplierRestController.class).loadSupplier(supplier.getId())).withSelfRel()));
        }
        return resources;
    }
}
