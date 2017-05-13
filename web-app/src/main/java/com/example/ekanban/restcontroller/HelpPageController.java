package com.example.ekanban.restcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by razamd on 3/30/2017.
 */
@Controller
@RequestMapping("/help")
public class HelpPageController {

    @GetMapping("/user")
    public String user(){
        return "user";
    }

    @GetMapping("/category")
    public String category(){
        return "category";
    }

    @GetMapping("/subCategory")
    public String subCategory(){
        return "subCategory";
    }

    @GetMapping("/section")
    public String section(){
        return "section";
    }

    @GetMapping("/supplier")
    public String supplier(){
        return "supplier";
    }

    @GetMapping("/product")
    public String product(){
        return "product";
    }
}
