package com.example.ics.restcontroller;

import com.example.ics.assembler.SubCategoryAssembler;
import com.example.ics.entity.SubCategory;
import com.example.ics.service.SubCategoryService;
import java.net.URI;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;


@RestController
@RequestMapping("/api/subCategories")
public class SubCategoryRestController {
    private final Logger logger = LoggerFactory.getLogger(SubCategoryRestController.class);
    
    @Autowired SubCategoryService subCategoryService;

    @Autowired SubCategoryAssembler subCategoryAssembler;
    
    @GetMapping
    public ResponseEntity<?> getSubCategories(Pageable pageable, PagedResourcesAssembler assembler){
        logger.debug("getSubCategories()");
        Page<SubCategory> page = subCategoryService.findPageAll(pageable);
        return new ResponseEntity<>(assembler.toResource(page, subCategoryAssembler), HttpStatus.OK);
    }
    
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getSubCategory(@PathVariable("id") Long id){
        logger.debug("getSubCategory(): id = {}",id);
        SubCategory subCategory = subCategoryService.findOne(id);
        if (subCategory == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(subCategoryAssembler.toResource(subCategory), HttpStatus.OK);
    }
   
    @PostMapping
    public ResponseEntity<Void> createSubCategory(@Valid @RequestBody SubCategory subCategory) {
        logger.debug("createSubCategory():\n {}", subCategory.toString());
        subCategory = subCategoryService.save(subCategory);
        Link selfLink = linkTo(SubCategoryRestController.class).slash(subCategory.getId()).withSelfRel();
        return ResponseEntity.created(URI.create(selfLink.getHref())).build();
    }
 
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateSubCategory(@PathVariable("id") long id,@Valid @RequestBody SubCategory subCategory) {
        logger.debug("updateSubCategory(): id = {} \n {}",id,subCategory);
        if (!subCategoryService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        subCategory.setId(id);  
        subCategory = subCategoryService.update(subCategory);
        return new ResponseEntity<>(subCategoryAssembler.toResource(subCategory), HttpStatus.OK);
    }
  
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteSubCategory(@PathVariable("id") long id) {
        logger.debug("deleteSubCategory(): id = {}",id);
        if (!subCategoryService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        subCategoryService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
