package com.example.ekanban.restcontroller;

import com.example.ekanban.dto.RestError;
import com.example.ekanban.service.ProductService;
import com.example.ekanban.util.ApiUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@RestController
@RequestMapping(ApiUrls.ROOT_URL_PRODUCTS)
public class ProductRestController {

    @Autowired ProductService productService;

    @PostMapping(ApiUrls.URL_PRODUCTS_SYNC)
    public ResponseEntity<?> syncProducts(){
        productService.sync();
        return  ResponseEntity.ok("SYNC SUCCESS");
    }

    @GetMapping(ApiUrls.URL_PRODUCTS_PRINTS_PRODUCT)
    public ResponseEntity<org.springframework.core.io.Resource> printBarcode(@PathVariable("productId") long productId, @RequestParam(value = "bins", defaultValue = "all") String bins){
        if (!productService.exists(productId)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        org.springframework.core.io.Resource file =  null;
        if (bins.equals("all")) {
            file = productService.printBarcode(productId);
        } else {
            file = productService.printBarcode(productId,bins);
        }
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .body(file);

    }

    @GetMapping(ApiUrls.URL_PRODUCTS_PRINTS)
    public ResponseEntity<?> printAllBarcode(@RequestParam(value = "productIds") String productIds){
        String regexp="^([0-9]+)|([0-9]+[0-9,]*[0-9]+)$";
        Pattern pattern = Pattern.compile(regexp);
        if (!pattern.matcher(productIds).matches() ){
            return new ResponseEntity<>(new RestError(400,400,"productIds param should be comma separated product ids","",""), HttpStatus.BAD_REQUEST);
        }

        org.springframework.core.io.Resource file =  productService.printAllBarcode(productIds);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .body(file);

    }

}
