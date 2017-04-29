package com.example.ics.assembler;

import com.example.ics.entity.Section;
import com.example.ics.restcontroller.SectionRestController;
import com.example.ics.restcontroller.SupplierRestController;
import org.springframework.hateoas.Resource;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SectionAssembler extends ResourceAssemblerSupport<Section, Resource>{

    public SectionAssembler() {
        super(SectionRestController.class, Resource.class);
    }

    @Override
    public Resource toResource(Section section) {       
        return new Resource<>(section, linkTo(SectionRestController.class).slash(section.getId()).withSelfRel()); 
    }

    @Override
    public List<Resource> toResources(Iterable<? extends Section> sections) {
        List<Resource> resources = new ArrayList<>();
        for(Section section : sections) {
            resources.add(new Resource<>(section, linkTo(methodOn(SectionRestController.class).loadSection(section.getId())).withSelfRel()));
        }
        return resources;
    }
}
