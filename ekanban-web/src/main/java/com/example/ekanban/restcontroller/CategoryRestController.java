package com.example.ekanban.restcontroller;

import com.example.ekanban.assembler.CategoryAssembler;
import com.example.ekanban.assembler.ProductAssembler;
import com.example.ekanban.assembler.SubCategoryAssembler;
import com.example.ekanban.dto.FieldError;
import com.example.ekanban.dto.ProductDto;
import com.example.ekanban.dto.RestError;
import com.example.ekanban.entity.Category;
import com.example.ekanban.entity.Product;
import com.example.ekanban.entity.SubCategory;
import com.example.ekanban.service.CategoryService;
import com.example.ekanban.service.ProductService;
import com.example.ekanban.service.SectionService;
import com.example.ekanban.service.SubCategoryService;
import com.example.ekanban.service.SupplierService;
import com.example.ekanban.util.ApiUrls;
import com.example.ekanban.util.MiscUtil;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

@RestController
@RequestMapping(ApiUrls.ROOT_URL_CATEGORIES)
public class CategoryRestController {

    private final Logger logger = LoggerFactory.getLogger(CategoryRestController.class);

    private final Mapper mapper;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;
    private final ProductService productService;
    private final SectionService sectionService;
    private final SupplierService supplierService;
    private final CategoryAssembler categoryAssembler;
    private final SubCategoryAssembler subCategoryAssembler;

    private final ProductAssembler productAssembler;

    @Autowired
    public CategoryRestController(
            Mapper mapper,
            CategoryService categoryService,
            SubCategoryService subCategoryService,
            ProductService productService,
            SectionService sectionService,
            SupplierService supplierService,
            CategoryAssembler categoryAssembler,
            SubCategoryAssembler subCategoryAssembler,
            ProductAssembler productAssembler
    ) {
        this.mapper = mapper;
        this.categoryService = categoryService;
        this.subCategoryService = subCategoryService;
        this.productService = productService;
        this.sectionService = sectionService;
        this.supplierService = supplierService;
        this.categoryAssembler = categoryAssembler;
        this.subCategoryAssembler = subCategoryAssembler;
        this.productAssembler = productAssembler;
    }

    ///////////////////////////////////////Category API/ /////////////////////////////////////
    @GetMapping
    public ResponseEntity<?> loadCategories(Pageable pageable, PagedResourcesAssembler assembler, @RequestParam(name ="expand", defaultValue = "false") Boolean expand) {
        logger.debug("getCategories(): expand = {}",expand);
        Page<Category> page = categoryService.findAllByPage(pageable,expand);
        return new ResponseEntity<>(assembler.toResource(page, categoryAssembler), HttpStatus.OK);
    }

    @GetMapping(ApiUrls.URL_CATEGORIES_CATEGORY)
    public ResponseEntity<?> loadCategory(@PathVariable("categoryId") Long categoryId) {
        logger.debug("getCategory(): categoryId = {}", categoryId);
        Category category = categoryService.findOne(categoryId, false, false);
        if (category == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(categoryAssembler.toResource(category), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody Category category) {
        logger.debug("createCategory():\n {}", category.toString());
        category = categoryService.save(category);
        Link selfLink = linkTo(CategoryRestController.class).slash(category.getId()).withSelfRel();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(selfLink.getHref()));
        return new ResponseEntity<>(categoryAssembler.toResource(category),headers,HttpStatus.CREATED);
    }

    @PutMapping(ApiUrls.URL_CATEGORIES_CATEGORY)
    public ResponseEntity<?> updateCategory(@PathVariable("categoryId") long categoryId, @Valid @RequestBody Category category) {
        logger.debug("updateCategory(): id = {} \n {}", categoryId, category);
        if (!categoryService.exists(categoryId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        category.setId(categoryId);
        category = categoryService.update(category);
        return new ResponseEntity<>(categoryAssembler.toResource(category), HttpStatus.OK);
    }

    @DeleteMapping(ApiUrls.URL_CATEGORIES_CATEGORY)
    public ResponseEntity<Void> deleteCategory(@PathVariable("categoryId") long categoryId) {
        logger.debug("deleteCategory(): categoryId = {}", categoryId);
        if (!categoryService.exists(categoryId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        categoryService.delete(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /////////////////////////////SubCategory API /////////////////////////
    @GetMapping(ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES)
    public ResponseEntity<?> loadCategorySubCategories(
            @PathVariable("categoryId") Long categoryId,
            Pageable pageable,
            PagedResourcesAssembler assembler) {
        logger.debug("loadCategorySubCategories(): categoryId = {}", categoryId);
        Category category = categoryService.findOne(categoryId, false, false);
        RestError error;
        if (category == null) {
            error = new RestError(404, 404, "Category with id = " + categoryId + " not found", "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        Page<SubCategory> page = subCategoryService.findPageByCategory(category, pageable);
        return new ResponseEntity<>(assembler.toResource(page, subCategoryAssembler), HttpStatus.OK);
    }

    @GetMapping(ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES_SUBCATEGORY)
    public ResponseEntity<?> loadCategorySubCategory(
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("subCategoryId") Long subCategoryId) {
        logger.debug("loadCategorySubCategory(): categoryId = {} , subCategoryId = {}", categoryId, subCategoryId);
        Category category = categoryService.findOne(categoryId, true, false);
        RestError error;
        if (category == null) {
            error = new RestError(404, 404, "Category with id = " + categoryId + " not found", "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        SubCategory subCategory = category.getSubCategoryList().stream()
                .filter(s -> s.getId().equals(subCategoryId))
                .findAny().orElse(null);
        if (subCategory == null) {
            String msg = "Sub Category with id = " + subCategoryId + " not found in " + category;
            error = new RestError(404, 404, msg, "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(subCategoryAssembler.toResource(subCategory), HttpStatus.OK);
    }

    @PostMapping(ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES)
    public ResponseEntity<?> createCategorySubCategory(
            @PathVariable("categoryId") Long categoryId,
            @Valid @RequestBody SubCategory subCategory
    ) {
        logger.debug("createCategorySubCategory(): categoryId= {} , subCategory = \n {}", categoryId, subCategory.toString());
        Category category = categoryService.findOne(categoryId, false, false);
        RestError error;
        if (category == null) {
            error = new RestError(404, 404, "Category with id = " + categoryId + " not found", "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        subCategory.setCategory(category);
        subCategory = subCategoryService.save(subCategory);
        Link selfLink= linkTo(methodOn(CategoryRestController.class).createCategorySubCategory(categoryId, subCategory)).slash(subCategory.getId()).withSelfRel();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(selfLink.getHref()));
        return new ResponseEntity<>(subCategoryAssembler.toResource(subCategory),headers,HttpStatus.CREATED);
    }

    @PutMapping(ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES_SUBCATEGORY)
    public ResponseEntity<?> updateCategorySubCategory(
            @PathVariable("categoryId") long categoryId,
            @PathVariable("subCategoryId") Long subCategoryId,
            @Valid @RequestBody SubCategory subCategory) {

        logger.debug("updateCategorySubCategory(): categoryId = {} , subCategoryId = {}, {}", categoryId, subCategoryId, subCategory);
        Category category = categoryService.findOne(categoryId, true, false);
        RestError error;
        if (category == null) {
            error = new RestError(404, 404, "Category with id = " + categoryId + " not found", "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        subCategory.setId(subCategoryId);
        if (!category.getSubCategoryList().contains(subCategory)) {
            String msg = "Sub Category with id = " + subCategoryId + " not found in " + category;
            error = new RestError(404, 404, msg, "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        subCategory = subCategoryService.update(subCategory);
        return new ResponseEntity<>(subCategoryAssembler.toResource(subCategory), HttpStatus.OK);
    }

    @DeleteMapping(ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES_SUBCATEGORY)
    public ResponseEntity<?> deleteCategorySubCategory(@PathVariable("categoryId") long categoryId, @PathVariable("subCategoryId") Long subCategoryId) {

        logger.debug("deleteCategorySubCategory(): categoryId = {} , subCategoryId = {}", categoryId, subCategoryId);
        Category category = categoryService.findOne(categoryId, true, false);
        RestError error;
        if (category == null) {
            error = new RestError(404, 404, "Category with id = " + categoryId + " not found", "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        SubCategory subCategory = category.getSubCategoryList().stream()
                .filter(s -> s.getId().equals(subCategoryId))
                .findAny().orElse(null);
        if (subCategory == null) {
            String msg = "Sub Category with id = " + subCategoryId + " not found in " + category;
            error = new RestError(404, 404, msg, "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        subCategoryService.delete(subCategoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /////////////////////////////Product API /////////////////////////
    @GetMapping(ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES_SUBCATEGORY_PRODUCTS)
    public ResponseEntity<?> loadCategorySubCategoryProducts(
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("subCategoryId") Long subCategoryId,
            Pageable pageable,
            PagedResourcesAssembler assembler) {
        logger.debug("loadCategorySubCategoryProducts(): categoryId = {}, subCategoryId = {}", categoryId, subCategoryId);
        Category category = categoryService.findOne(categoryId, true, false);
        RestError error;
        if (category == null) {
            error = new RestError(404, 404, "Category with id = " + categoryId + " not found", "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        SubCategory subCategory = category.getSubCategoryList().stream()
                .filter(s -> s.getId().equals(subCategoryId))
                .findAny().orElse(null);
        if (subCategory == null) {
            String msg = "Sub Category with id = " + subCategoryId + " not found in " + category;
            error = new RestError(404, 404, msg, "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        Page<ProductDto> page = productService.findPageBySubCategory(subCategory, pageable);
        return new ResponseEntity<>(assembler.toResource(page, productAssembler), HttpStatus.OK);
    }

    @GetMapping(ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES_SUBCATEGORY_PRODUCTS_PRODUCT)
    public ResponseEntity<?> loadCategorySubCategoryProduct(
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("subCategoryId") Long subCategoryId,
            @PathVariable("productId") Long productId) {
        logger.debug("loadCategorySubCategory(): categoryId = {} , subCategoryId = {}", categoryId, subCategoryId);
        Category category = categoryService.findOne(categoryId, true, true);
        RestError error;
        if (category == null) {
            error = new RestError(404, 404, "Category with id = " + categoryId + " not found", "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        SubCategory subCategory = category.getSubCategoryList().stream()
                .filter(s -> s.getId().equals(subCategoryId))
                .findAny().orElse(null);
        if (subCategory == null) {
            String msg = "Sub Category with id = " + subCategoryId + " not found in " + category;
            error = new RestError(404, 404, msg, "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        Product product = subCategory.getProductList().stream()
                .filter(p -> p.getId().equals(productId))
                .findAny().orElse(null);
        if (product == null) {
            String msg = "Product with id = " + productId + " not found in " + subCategory + " and  " + category;
            error = new RestError(404, 404, msg, "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productAssembler.toResource(mapper.map(product, ProductDto.class)), HttpStatus.OK);
    }

    @PostMapping(ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES_SUBCATEGORY_PRODUCTS)
    public ResponseEntity<?> createCategorySubCategoryProduct(
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("subCategoryId") Long subCategoryId,
            @Valid @RequestBody ProductDto productDto
    ) {
        logger.debug("createCategorySubCategoryProduct(): categoryId= {} , subCategoryId = {}, {}", categoryId, subCategoryId, productDto);
        /*///////Checking for Duplicate ItemCode///////*/
        if (productService.exists(productDto.getItemCode().trim())) {
            String msg = "Duplicate Item Code.";
            RestError error = new RestError(409, 409, msg, "", "");
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }
        /*//////////////Validating resources/////////////////*/
        List<String> invalidSections = MiscUtil.findInvalidResources(productDto.getSections(), SectionRestController.class);
        List<String> invalidSuppliers = MiscUtil.findInvalidResources(productDto.getSuppliers(), SupplierRestController.class);
        List<FieldError> errors = new ArrayList<>();
        if (!(invalidSections == null || invalidSections.isEmpty())) {
            errors.add(new FieldError("sections", MiscUtil.toStringList(productDto.getSections()), "Resource/Resources " + MiscUtil.toStringList(invalidSections) + " is/are inavlid."));
        }
        if (!(invalidSuppliers == null || invalidSuppliers.isEmpty())) {
            errors.add(new FieldError("suppliers", MiscUtil.toStringList(productDto.getSuppliers()), "Resource/Resources " + MiscUtil.toStringList(invalidSuppliers) + " is/are inavlid."));
        }
        if (!errors.isEmpty()) {
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        /*///////////////////checking if category and sub category exist ////////////*/
        Category category = categoryService.findOne(categoryId, true, false);
        RestError error;
        if (category == null) {
            error = new RestError(404, 404, "Category with id = " + categoryId + " not found", "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

        }
        SubCategory subCategory = category.getSubCategoryList().stream()
                .filter(s -> s.getId().equals(subCategoryId))
                .findAny().orElse(null);
        if (subCategory == null) {
            String msg = "Sub Category with id = " + subCategoryId + " not found in Category: " + category.getName();
            error = new RestError(404, 404, msg, "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        /*////////////checking if section resources got in ProductDto object exist in db /////// */
        List<String> sectionsNotFound = null;
        if (productDto.getSections() != null) {
            sectionsNotFound = productDto.getSections().stream()
                    .filter(uri -> sectionService.exists(MiscUtil.extractIdFromUri(uri)) ? false : true)
                    .collect(Collectors.toList());
        }
        if (!(sectionsNotFound == null || sectionsNotFound.isEmpty())) {
            String msg = "Resource/Resources " + MiscUtil.toStringList(sectionsNotFound) + " not found";
            error = new RestError(404, 404, msg, "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        /*///////////////////////////////////////////////////////*/
        /*////////////checking if supplier resources got in ProductDto object exist in db /////// */
        List<String> suppliersNotFound = null;
        if (productDto.getSections() != null) {
            suppliersNotFound = productDto.getSuppliers().stream()
                    .filter(uri -> supplierService.exists(MiscUtil.extractIdFromUri(uri)) ? false : true)
                    .collect(Collectors.toList());
        }
        if (!(suppliersNotFound == null || suppliersNotFound.isEmpty())) {
            String msg = "Resource/Resources " + MiscUtil.toStringList(suppliersNotFound) + " not found";
            error = new RestError(404, 404, msg, "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        Product product = mapper.map(productDto, Product.class);
        product.setSubCategory(subCategory);
        /*//////get list of section resource id's corresponding to the resources*/
        List<Long> sectionIdList = null;
        if (productDto.getSections() != null) {
            sectionIdList = productDto.getSections().stream()
                    .map(uri -> MiscUtil.extractIdFromUri(uri))
                    .collect(Collectors.toList());
        }
        /*//////get list of section resource id's corresponding to the resources////////*/
        List<Long> supplierIdList = null;
        if (productDto.getSuppliers() != null) {
            supplierIdList = productDto.getSuppliers().stream()
                    .map(uri -> MiscUtil.extractIdFromUri(uri))
                    .collect(Collectors.toList());
        }

        productDto = productService.save(product,sectionIdList,supplierIdList);
        Link selfLink= linkTo(methodOn(CategoryRestController.class).createCategorySubCategoryProduct(categoryId, subCategoryId, productDto)).slash(productDto.getId()).withSelfRel();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(selfLink.getHref()));
        return new ResponseEntity<>(productAssembler.toResource(productDto),headers,HttpStatus.CREATED);

    }

    @PutMapping(ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES_SUBCATEGORY_PRODUCTS_PRODUCT)
    public ResponseEntity<?> updateCategorySubCategoryProduct(
            @PathVariable("categoryId") long categoryId,
            @PathVariable("subCategoryId") Long subCategoryId,
            @PathVariable("productId") Long productId,
            @Valid @RequestBody ProductDto productDto) {

        logger.debug("updateCategorySubCategoryProduct(): categoryId= {} , subCategoryId = {}, productId = {}, {}", categoryId, subCategoryId,productId, productDto);

        /*//////////////Validating resources/////////////////*/
        List<String> invalidSections = MiscUtil.findInvalidResources(productDto.getSections(), SectionRestController.class);
        List<String> invalidSuppliers = MiscUtil.findInvalidResources(productDto.getSuppliers(), SupplierRestController.class);
        List<FieldError> errors = new ArrayList<>();
        if (!(invalidSections == null || invalidSections.isEmpty())) {
            errors.add(new FieldError("sections", MiscUtil.toStringList(productDto.getSections()), "Resource/Resources " + MiscUtil.toStringList(invalidSections) + " is/are inavlid."));
        }
        if (!(invalidSuppliers == null || invalidSuppliers.isEmpty())) {
            errors.add(new FieldError("suppliers", MiscUtil.toStringList(productDto.getSuppliers()), "Resource/Resources " + MiscUtil.toStringList(invalidSuppliers) + " is/are inavlid."));
        }
        if (!errors.isEmpty()) {
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        /*////////////////////////////////////////////////////////////////////////*/
        /*///////////////////checking if category, sub category and product exist ////////////*/
        Category category = categoryService.findOne(categoryId, true, false);
        RestError error;
        if (category == null) {
            error = new RestError(404, 404, "Category with id = " + categoryId + " not found", "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

        }
        SubCategory subCategory = category.getSubCategoryList().stream()
                                          .filter(s -> s.getId().equals(subCategoryId))
                                          .findAny().orElse(null);
        if (subCategory == null) {
            String msg = "Sub Category with id = " + subCategoryId + " not found in Category: " + category.getName();
            error = new RestError(404, 404, msg, "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        Product pdct = subCategory.getProductList().stream()
                .filter(p -> p.getId().equals(productId))
                .findAny().orElse(null);

        if (pdct == null) {
            String msg = "Product with id = " + productId + " not found in SubCategory:  " + subCategory.getName() + ", Category: " + category.getName();
            error = new RestError(404, 404, msg, "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

//        if (productService.exists(productDto.getItemCode().trim())) {
//            String msg = "Duplicate Item Code.";
//            error = new RestError(409, 409, msg, "", "");
//            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
//        }

        /*///////////////////////////////////////////////////////*/
        /*////////////checking if section resources got in ProductDto object exist in db /////// */
        List<String> sectionsNotFound = null;
        if (productDto.getSections() != null) {
            sectionsNotFound = productDto.getSections().stream()
                                         .filter(uri -> sectionService.exists(MiscUtil.extractIdFromUri(uri)) ? false : true)
                                         .collect(Collectors.toList());
        }
        if (!(sectionsNotFound == null || sectionsNotFound.isEmpty())) {
            String msg = "Resource/Resources " + MiscUtil.toStringList(sectionsNotFound) + " not found";
            error = new RestError(404, 404, msg, "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        /*///////////////////////////////////////////////////////*/
        /*////////////checking if supplier resources got in ProductDto object exist in db /////// */
        List<String> suppliersNotFound = null;
        if (productDto.getSections() != null) {
            suppliersNotFound = productDto.getSuppliers().stream()
                                          .filter(uri -> supplierService.exists(MiscUtil.extractIdFromUri(uri)) ? false : true)
                                          .collect(Collectors.toList());
        }
        if (!(suppliersNotFound == null || suppliersNotFound.isEmpty())) {
            String msg = "Resource/Resources " + MiscUtil.toStringList(suppliersNotFound) + " not found";
            error = new RestError(404, 404, msg, "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        Product product = mapper.map(productDto, Product.class);
        product.setSubCategory(subCategory);
        /*//////get list of section resource id's corresponding to the resources*/
        List<Long> sectionIdList = null;
        if (productDto.getSections() != null) {
            sectionIdList = productDto.getSections().stream()
                                      .map(uri -> MiscUtil.extractIdFromUri(uri))
                                      .collect(Collectors.toList());
        }
        /*//////get list of section resource id's corresponding to the resources*/
        List<Long> supplierIdList = null;
        if (productDto.getSuppliers() != null) {
            supplierIdList = productDto.getSuppliers().stream()
                                       .map(uri -> MiscUtil.extractIdFromUri(uri))
                                       .collect(Collectors.toList());
        }

        productDto = productService.update(product,sectionIdList,supplierIdList);
        return new ResponseEntity<>(productAssembler.toResource(productDto),HttpStatus.OK);
    }

    @DeleteMapping(ApiUrls.URL_CATEGORIES_CATEGORY_SUBCATEGORIES_SUBCATEGORY_PRODUCTS_PRODUCT)
    public ResponseEntity<?> deleteCategorySubCategoryProduct(
            @PathVariable("categoryId") long categoryId,
            @PathVariable("subCategoryId") Long subCategoryId,
            @PathVariable("productId") Long productId) {

        logger.debug("deleteCategorySubCategoryProduct(): categoryId = {} , subCategoryId = {}, productId = {}",categoryId, subCategoryId, productId );

        /*///////////////////checking if category, sub category and product exist ////////////*/
        Category category = categoryService.findOne(categoryId, true, false);
        RestError error;
        if (category == null) {
            error = new RestError(404, 404, "Category with id = " + categoryId + " not found", "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

        }
        SubCategory subCategory = category.getSubCategoryList().stream()
                                          .filter(s -> s.getId().equals(subCategoryId))
                                          .findAny().orElse(null);
        if (subCategory == null) {
            String msg = "Sub Category with id = " + subCategoryId + " not found in Category: " + category.getName();
            error = new RestError(404, 404, msg, "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        Product pdct = subCategory.getProductList().stream()
                                  .filter(p -> p.getId().equals(productId))
                                  .findAny().orElse(null);

        if (pdct == null) {
            String msg = "Product with id = " + productId + " not found in SubCategory:  " + subCategory.getName() + ", Category: " + category.getName();
            error = new RestError(404, 404, msg, "", "");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        /*///////////////////////////////////////////////////////*/

        productService.delete(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
