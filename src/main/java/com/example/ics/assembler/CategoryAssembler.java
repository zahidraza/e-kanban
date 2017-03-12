/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ics.assembler;

import com.example.ics.entity.Category;
import com.example.ics.restcontroller.CategoryRestController;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class CategoryAssembler extends ResourceAssemblerSupport<Category, Resource>{

    public CategoryAssembler() {
        super(CategoryRestController.class, Resource.class);
    }

    @Override
    public Resource toResource(Category category) {
        Pageable pageable = null;
        PagedResourcesAssembler assembler = null;
        Collection<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(CategoryRestController.class).loadCategory(category.getId())).withSelfRel());
        links.add(linkTo(methodOn(CategoryRestController.class).loadCategorySubCategories(category.getId(),pageable,assembler)).withRel("subCategoryList"));
        return new Resource<>(category, links); 
    }
    
}
