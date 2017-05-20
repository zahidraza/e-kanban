package com.example.ekanban.assembler;

import com.example.ekanban.dto.OrderDto;
import com.example.ekanban.entity.Order;
import com.example.ekanban.restcontroller.OrderRestController;
import org.springframework.hateoas.Resource;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderAssembler extends ResourceAssemblerSupport<OrderDto, Resource>{

    public OrderAssembler() {
        super(OrderRestController.class, Resource.class);
    }

    @Override
    public Resource toResource(OrderDto order) {
        return new Resource<>(order, linkTo(OrderRestController.class).slash(order.getId()).withSelfRel());
    }

    @Override
    public List<Resource> toResources(Iterable<? extends OrderDto> orders) {
        List<Resource> resources = new ArrayList<>();
        for(OrderDto order : orders) {
            resources.add(new Resource<>(order, linkTo(methodOn(OrderRestController.class).loadOrder(order.getId())).withSelfRel()));
        }
        return resources;
    }
}
