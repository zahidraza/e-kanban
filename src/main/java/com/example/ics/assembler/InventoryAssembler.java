package com.example.ics.assembler;

import com.example.ics.dto.InventoryDto;
import com.example.ics.restcontroller.InventoryRestController;
import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class InventoryAssembler extends ResourceAssemblerSupport<InventoryDto, Resource>{

    public InventoryAssembler(){
        super(InventoryRestController.class, Resource.class);
    }

    @Override
    public Resource toResource(InventoryDto inventoryDto) {
        Link selfLink = linkTo(InventoryRestController.class).slash(inventoryDto.getId()).withSelfRel();
        return new Resource<>(inventoryDto, linkTo(InventoryRestController.class).slash(inventoryDto.getId()).withSelfRel());
    }

    @Override
    public List<Resource> toResources(Iterable<? extends InventoryDto> inventoryList) {
        List<Resource> resources = new ArrayList<>();
        for(InventoryDto inventory : inventoryList) {
            resources.add(toResource(inventory));
        }
        return resources;
    }
}
