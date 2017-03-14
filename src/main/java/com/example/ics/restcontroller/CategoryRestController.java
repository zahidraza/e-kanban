package com.example.ics.restcontroller;

import com.example.ics.assembler.CategoryAssembler;
import com.example.ics.assembler.ProductAssembler;
import com.example.ics.assembler.SubCategoryAssembler;
import com.example.ics.dto.ProductDto;
import com.example.ics.dto.RestError;
import com.example.ics.entity.Category;
import com.example.ics.entity.Product;
import com.example.ics.entity.SubCategory;
import com.example.ics.service.CategoryService;
import com.example.ics.service.ProductService;
import com.example.ics.service.SubCategoryService;
import com.example.ics.util.ApiUrls;
import java.net.URI;
import java.util.Set;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping(value = ApiUrls.ROOT_URL_CATEGORIES)
public class CategoryRestController {

    private final Logger logger = LoggerFactory.getLogger(CategoryRestController.class);
    
    Mapper mapper;
    
    CategoryService categoryService;
    
    SubCategoryService subCategoryService;

    CategoryAssembler categoryAssembler;
    
    SubCategoryAssembler subCategoryAssembler;
    
    ProductService productService;
    
    ProductAssembler productAssembler;
    
    @Autowired
    public CategoryRestController(
            Mapper mapper,
            CategoryService categoryService,
            SubCategoryService subCategoryService,
            CategoryAssembler categoryAssembler,
            SubCategoryAssembler subCategoryAssembler,
            ProductService productService,
            ProductAssembler productAssembler
    ){
        this.mapper = mapper;
        this.categoryService = categoryService;
        this.subCategoryService = subCategoryService;
        this.categoryAssembler = categoryAssembler;
        this.subCategoryAssembler = subCategoryAssembler;
        this.productService = productService;
        this.productAssembler = productAssembler;
    }
    
    ///////////////////////////////////////Category API/ /////////////////////////////////////
    
    @GetMapping
    public ResponseEntity<?> loadCategories(Pageable pageable, PagedResourcesAssembler assembler){
        logger.debug("getCategories()");
        Page<Category> page = categoryService.findAllByPage(pageable);
        return new ResponseEntity<>(assembler.toResource(page, categoryAssembler), HttpStatus.OK);
    }
    
    @GetMapping(value = ApiUrls.URL_CATEGORIES_CATEGORY)
    public ResponseEntity<?> loadCategory(@PathVariable("categoryId") Long categoryId){
        logger.debug("getCategory(): categoryId = {}",categoryId);
        Category category = categoryService.findOne(categoryId,false,false);
        if (category == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(categoryAssembler.toResource(category), HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<Void> createCategory(@Validated @RequestBody Category category) {
        logger.debug("createCategory():\n {}", category.toString());
        category = categoryService.save(category);
        Link selfLink = linkTo(CategoryRestController.class).slash(category.getId()).withSelfRel();
        return ResponseEntity.created(URI.create(selfLink.getHref())).build();
    }
    
    @PutMapping(value = ApiUrls.URL_CATEGORIES_CATEGORY)
    public ResponseEntity<?> updateCategory(@PathVariable("categoryId") long categoryId,@Validated @RequestBody Category category) {
        logger.debug("updateCategory(): id = {} \n {}",categoryId,category);
        if (!categoryService.exists(categoryId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        category.setId(categoryId);  
        category = categoryService.update(category);
        return new ResponseEntity<>(categoryAssembler.toResource(category), HttpStatus.OK);
    }
  
    @DeleteMapping(value = ApiUrls.URL_CATEGORIES_CATEGORY)
    public ResponseEntity<Void> deleteCategory(@PathVariable("categoryId") long categoryId) {
        logger.debug("deleteCategory(): categoryId = {}",categoryId);
        if (!categoryService.exists(categoryId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        categoryService.delete(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    /////////////////////////////SubCategory API /////////////////////////
    
    @GetMapping(value = ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES)
    public ResponseEntity<?> loadCategorySubCategories(
            @PathVariable("categoryId") Long categoryId,
            Pageable pageable, 
            PagedResourcesAssembler assembler) {
        logger.debug("loadCategorySubCategories(): categoryId = {}",categoryId );
        Category category = categoryService.findOne(categoryId,false,false);
        if(category == null){
            return new ResponseEntity<>("Category with id = " + categoryId + " not found", HttpStatus.NOT_FOUND);
        }
        Page<SubCategory> page = subCategoryService.findPageByCategory(category, pageable);
        return new ResponseEntity<>(assembler.toResource(page, subCategoryAssembler), HttpStatus.OK);
    }
    
    @GetMapping(value = ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES_SUBCATEGORY)
    public ResponseEntity<?> loadCategorySubCategory(
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("subCategoryId") Long subCategoryId) {
        logger.debug("loadCategorySubCategory(): categoryId = {} , subCategoryId = {}",categoryId, subCategoryId );
        Category category = categoryService.findOne(categoryId,true,false);
        RestError error;
        if(category == null){
            error = new RestError(404, 404, "Category with id = " + categoryId + " not found", "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        SubCategory subCategory = category.getSubCategoryList().stream()
                .filter(s -> s.getId().equals(subCategoryId))
                .findAny().orElse(null);
        if(subCategory == null){
            String msg = "Sub Category with id = " + subCategoryId + " not found in " + category;
            error = new RestError(404, 404,msg, "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(subCategoryAssembler.toResource(subCategory), HttpStatus.OK);
    }
    
    @PostMapping(value = ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES)
    public ResponseEntity<?> createCategorySubCategory(
            @PathVariable("categoryId") Long categoryId,
            @Validated @RequestBody SubCategory subCategory
            ) {
        logger.debug("createCategorySubCategory(): categoryId= {} , subCategory = \n {}", categoryId, subCategory.toString());
        Category category = categoryService.findOne(categoryId,false,false);
        if(category == null){
            return new ResponseEntity<>("Category with id = " + categoryId + " not found", HttpStatus.NOT_FOUND);
        }
        subCategory.setCategory(category);
        subCategory = subCategoryService.save(subCategory);
        Link link = linkTo(methodOn(CategoryRestController.class).createCategorySubCategory(categoryId, subCategory)).slash(subCategory.getId()).withSelfRel();
        return ResponseEntity.created(URI.create(link.getHref())).build();
    }
   
    @PutMapping(value = ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES_SUBCATEGORY)
    public ResponseEntity<?> updateCategorySubCategory(
            @PathVariable("categoryId") long categoryId,
            @PathVariable("subCategoryId") Long subCategoryId,
            @Validated @RequestBody SubCategory subCategory) {
        
        logger.debug("updateCategorySubCategory(): categoryId = {} , subCategoryId = {}, {}",categoryId, subCategoryId,subCategory );
        Category category = categoryService.findOne(categoryId,true,false);
        RestError error;
        if(category == null){
            error = new RestError(404, 404, "Category with id = " + categoryId + " not found", "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        subCategory.setId(subCategoryId);
        if(!category.getSubCategoryList().contains(subCategory)){
            String msg = "Sub Category with id = " + subCategoryId + " not found in " + category;
            error = new RestError(404, 404,msg, "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        subCategory = subCategoryService.update(subCategory);
        return new ResponseEntity<>(subCategoryAssembler.toResource(subCategory), HttpStatus.OK);
    }
  
    @DeleteMapping(value = ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES_SUBCATEGORY)
    public ResponseEntity<?> deleteCategorySubCategory(@PathVariable("categoryId") long categoryId,@PathVariable("subCategoryId") Long subCategoryId) {
        
        logger.debug("deleteCategorySubCategory(): categoryId = {} , subCategoryId = {}",categoryId, subCategoryId );
        Category category = categoryService.findOne(categoryId,true,false);
        RestError error;
        if(category == null){
            error = new RestError(404, 404, "Category with id = " + categoryId + " not found", "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        SubCategory subCategory = category.getSubCategoryList().stream()
                .filter(s -> s.getId().equals(subCategoryId))
                .findAny().orElse(null);
        if(subCategory == null){
            String msg = "Sub Category with id = " + subCategoryId + " not found in " + category;
            error = new RestError(404, 404,msg, "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        subCategoryService.delete(subCategoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    /////////////////////////////Product API /////////////////////////
 
    @GetMapping(value = ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES_SUBCATEGORY_PRODUCTS)
    public ResponseEntity<?> loadCategorySubCategoryProducts(
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("subCategoryId") Long subCategoryId,
            Pageable pageable, 
            PagedResourcesAssembler assembler) {
        logger.debug("loadCategorySubCategoryProducts(): categoryId = {}, subCategoryId = {}",categoryId, subCategoryId);
        Category category = categoryService.findOne(categoryId,true,false);
        RestError error;
        if(category == null){
            error = new RestError(404, 404, "Category with id = " + categoryId + " not found", "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        SubCategory subCategory = category.getSubCategoryList().stream()
                .filter(s -> s.getId().equals(subCategoryId))
                .findAny().orElse(null);
        if(subCategory == null){
            String msg = "Sub Category with id = " + subCategoryId + " not found in " + category;
            error = new RestError(404, 404,msg, "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        
        Page<ProductDto> page = productService.findPageBySubCategory(subCategory, pageable);
        return new ResponseEntity<>(assembler.toResource(page, productAssembler), HttpStatus.OK);
    }
    
    @GetMapping(value = ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES_SUBCATEGORY_PRODUCTS_PRODUCT)
    public ResponseEntity<?> loadCategorySubCategoryProduct(
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("subCategoryId") Long subCategoryId,
            @PathVariable("productId") Long productId) {
        logger.debug("loadCategorySubCategory(): categoryId = {} , subCategoryId = {}",categoryId, subCategoryId );
        Category category = categoryService.findOne(categoryId,true,true);
        RestError error;
        if(category == null){
            error = new RestError(404, 404, "Category with id = " + categoryId + " not found", "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        SubCategory subCategory = category.getSubCategoryList().stream()
                .filter(s -> s.getId().equals(subCategoryId))
                .findAny().orElse(null);
        if(subCategory == null){
            String msg = "Sub Category with id = " + subCategoryId + " not found in " + category;
            error = new RestError(404, 404,msg, "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        Product product = subCategory.getProductList().stream()
                            .filter(p -> p.getId().equals(productId))
                            .findAny().orElse(null);
        if(product == null){
            String msg = "Product with id = "+ productId + " not found in " + subCategory + " and  " + category;
            error = new RestError(404, 404,msg, "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productAssembler.toResource(mapper.map(product, ProductDto.class)), HttpStatus.OK);
    }
    
    @PostMapping(value = ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES_SUBCATEGORY_PRODUCTS)
    public ResponseEntity<?> createCategorySubCategoryProduct(
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("subCategoryId") Long subCategoryId,
            @Validated @RequestBody ProductDto productDto
            ) {
        logger.debug("createCategorySubCategoryProduct(): categoryId= {} , subCategoryId = {}, {}", categoryId, subCategoryId, productDto);
        Category category = categoryService.findOne(categoryId,true,false);
        RestError error;
        if(category == null){
            error = new RestError(404, 404,"Category with id = " + categoryId + " not found", "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
            
        }
        SubCategory subCategory = category.getSubCategoryList().stream()
                .filter(s -> s.getId().equals(subCategoryId))
                .findAny().orElse(null);
        if(subCategory == null){
            String msg = "Sub Category with id = " + subCategoryId + " not found in " + category;
            error = new RestError(404, 404,msg, "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        Product product = mapper.map(productDto, Product.class);
        product.setSubCategory(subCategory);

        product = productService.save(product);
        Link link = linkTo(methodOn(CategoryRestController.class).createCategorySubCategoryProduct(categoryId, subCategoryId,productDto)).slash(product.getId()).withSelfRel();
        return ResponseEntity.created(URI.create(link.getHref())).build();
    }
   
//    @PutMapping(value = ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES_SUBCATEGORY)
//    public ResponseEntity<?> updateCategorySubCategory(
//            @PathVariable("categoryId") long categoryId,
//            @PathVariable("subCategoryId") Long subCategoryId,
//            @Validated @RequestBody SubCategory subCategory) {
//        
//        logger.debug("updateCategorySubCategory(): categoryId = {} , subCategoryId = {}, {}",categoryId, subCategoryId,subCategory );
//        Category category = categoryService.findOne(categoryId,true);
//        RestError error;
//        if(category == null){
//            error = new RestError(404, 404, "Category with id = " + categoryId + " not found", "", "");
//            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//        }
//        Set<SubCategory> subCategories = category.getSubCategoryList();
//        subCategory.setId(subCategoryId);
//        if(!subCategories.contains(subCategory)){
//            String msg = "Sub Category with id = " + subCategoryId + " not found in " + category;
//            error = new RestError(404, 404,msg, "", "");
//            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//        }
//
//        subCategory = subCategoryService.update(subCategory);
//        return new ResponseEntity<>(subCategoryAssembler.toResource(subCategory), HttpStatus.OK);
//    }
//  
//    @DeleteMapping(value = ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES_SUBCATEGORY)
//    public ResponseEntity<?> deleteCategorySubCategory(@PathVariable("categoryId") long categoryId,@PathVariable("subCategoryId") Long subCategoryId) {
//        
//        logger.debug("deleteCategorySubCategory(): categoryId = {} , subCategoryId = {}",categoryId, subCategoryId );
//        Category category = categoryService.findOne(categoryId,true);
//        RestError error;
//        if(category == null){
//            error = new RestError(404, 404, "Category with id = " + categoryId + " not found", "", "");
//            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//        }
//        Set<SubCategory> subCategories = category.getSubCategoryList();
//        SubCategory subCategory = subCategories.stream()
//                .filter(s -> s.getId().equals(subCategoryId))
//                .findAny().orElse(null);
//        if(subCategory == null){
//            String msg = "Sub Category with id = " + subCategoryId + " not found in " + category;
//            error = new RestError(404, 404,msg, "", "");
//            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//        }
//        subCategoryService.delete(subCategoryId);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
    
}
