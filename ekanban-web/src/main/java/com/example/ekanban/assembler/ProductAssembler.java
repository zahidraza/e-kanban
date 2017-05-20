/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ekanban.assembler;

import com.example.ekanban.dto.ProductDto;
import com.example.ekanban.restcontroller.CategoryRestController;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

/**
 *
 * @author razamd
 */
@Component
public class ProductAssembler extends ResourceAssemblerSupport<ProductDto, Resource>{

    public ProductAssembler() {
        super(CategoryRestController.class, Resource.class);
    }

    @Override
    public Resource toResource(ProductDto productDto) {       
        Collection<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(CategoryRestController.class)
                        .loadCategorySubCategoryProduct(productDto.getCategory().getId(),productDto.getSubCategory().getId(),productDto.getId()))
                        .withSelfRel()
        );
        return new Resource<>(productDto, links);
    }
    
}
