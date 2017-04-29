package com.example.ics.restcontroller;

import com.example.ics.service.ProductService;
import com.example.ics.util.ApiUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrls.ROOT_URL_PRODUCTS)
public class ProductRestController {

    @Autowired ProductService productService;

    @PostMapping(ApiUrls.URL_PRODUCTS_SYNC)
    public ResponseEntity<?> syncProducts(@RequestParam(value = "firstSync", defaultValue = "false") Boolean fistSync){
        productService.sync(fistSync);
        return  ResponseEntity.ok("SYNC SUCCESS");
    }
}
