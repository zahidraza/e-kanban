/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ics.assembler;

import com.example.ics.entity.SubCategory;
import com.example.ics.restcontroller.SubCategoryRestController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class SubCategoryAssembler extends ResourceAssemblerSupport<SubCategory, Resource>{

    public SubCategoryAssembler() {
        super(SubCategoryRestController.class, Resource.class);
    }

    @Override
    public Resource toResource(SubCategory subCategory) {
        return new Resource<>(subCategory, linkTo(methodOn(SubCategoryRestController.class).getSubCategory(subCategory.getId())).withSelfRel()); 
    }
    
}