package com.example.ics.restcontroller;

import com.example.ics.dto.ProductCsv;
import com.example.ics.entity.Product;
import com.example.ics.service.ProductService;
import com.example.ics.util.ApiUrls;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by razamd on 3/29/2017.
 */
@RestController
@RequestMapping(ApiUrls.ROOT_URL_UPLOADS)
public class UploadRestController {

    private final Logger logger = LoggerFactory.getLogger(UploadRestController.class);

    @Autowired
    Mapper mapper;

    @Autowired ProductService productService;

    @PostMapping(ApiUrls.URL_UPLOADS_PRODUCTS)
    public ResponseEntity<?> uploadProducts(@RequestParam("file") MultipartFile file){
        logger.debug("uploadProducts()");
        if (! (file.getOriginalFilename().contains("xlsx") || file.getOriginalFilename().contains("xls"))) {
            return new ResponseEntity<>(new Error("FILE_NOT_SUPPORTED"), HttpStatus.CONFLICT);
        }
        productService.addProductsBatch(file);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(){

        ProductCsv productCsv = new ProductCsv("Test Product","Test Category 1","Test SubCategory 1","Test desc " +
                "1","Test Section 1",20.45,
                "CODE1",1,2,3,4,"KG","DOZEN",1.5,"Test Supplier","test Contact persion","LOCAL",1000L,50);


        Product product = mapper.map(productCsv, Product.class);

        System.out.println(product);

        return ResponseEntity.ok("OK");
    }
}
