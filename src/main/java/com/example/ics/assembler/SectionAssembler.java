package com.example.ics.assembler;

import com.example.ics.entity.Section;
import com.example.ics.restcontroller.SectionRestController;
import org.springframework.hateoas.Resource;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class SectionAssembler extends ResourceAssemblerSupport<Section, Resource>{

    public SectionAssembler() {
        super(SectionRestController.class, Resource.class);
    }

    @Override
    public Resource toResource(Section section) {       
        return new Resource<>(section, linkTo(SectionRestController.class).slash(section.getId()).withSelfRel()); 
    }
    
}
