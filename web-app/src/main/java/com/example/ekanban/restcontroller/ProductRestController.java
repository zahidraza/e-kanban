package com.example.ekanban.restcontroller;

import com.example.ekanban.service.ProductService;
import com.example.ekanban.util.ApiUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiUrls.ROOT_URL_PRODUCTS)
public class ProductRestController {

    @Autowired ProductService productService;

    @PostMapping(ApiUrls.URL_PRODUCTS_SYNC)
    public ResponseEntity<?> syncProducts(@RequestParam(value = "firstSync", defaultValue = "false") Boolean fistSync){
        productService.sync(fistSync);
        return  ResponseEntity.ok("SYNC SUCCESS");
    }

    @GetMapping(ApiUrls.URL_PRODUCTS_PRODUCT)
    public ResponseEntity<org.springframework.core.io.Resource> printBarcode(@PathVariable("productId") long productId, @RequestParam("bins") String bins){
        if (!productService.exists(productId)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        org.springframework.core.io.Resource file = productService.printBarcode(productId,bins);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .body(file);

    }
}
