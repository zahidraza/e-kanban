package com.example.ekanban.page.converter;

import com.example.ekanban.dto.ProductDto;
import com.example.ekanban.entity.Product;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter implements Converter<Product, ProductDto>{

    @Autowired Mapper mapper;
    
    @Override
    public ProductDto convert(Product source) {
        return mapper.map(source, ProductDto.class);
    }

    
}
