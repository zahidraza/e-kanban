package com.example.ics.restcontroller;

import com.example.ics.util.ApiUrls;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by razamd on 3/29/2017.
 */
@RestController
@RequestMapping(ApiUrls.ROOT_URL_UPLOADS)
public class UploadRestController {

    @PostMapping(ApiUrls.URL_UPLOADS_PRODUCTS)
    public ResponseEntity<?> uploadProducts(@RequestParam("file") MultipartFile file){

        if (! (file.getOriginalFilename().contains("xlsx") || file.getOriginalFilename().contains("xls"))) {
            return new ResponseEntity<>(new Error("FILE_NOT_SUPPORTED"), HttpStatus.CONFLICT);
        }
        return null;
    }
}
