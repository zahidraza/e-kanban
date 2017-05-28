package com.example.ekanban.restcontroller;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AppController {
    
    @GetMapping("/")
    public ResponseEntity<?> root(){
        ResourceSupport resources = new ResourceSupport();
        ControllerLinkBuilder linkBuilder = linkTo(AppController.class);
        resources.add(linkBuilder.slash("users").withRel("users"));
        resources.add(linkBuilder.slash("categories").withRel("categories"));
        resources.add(linkBuilder.slash("sections").withRel("section"));
        resources.add(linkBuilder.slash("suppliers").withRel("suppliers"));
        return ResponseEntity.ok(resources);
    }
}
