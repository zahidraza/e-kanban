package com.example.ekanban.dozer.converter;

import com.example.ekanban.entity.Product;
import org.dozer.DozerConverter;

public class ProductCategoryConverter extends DozerConverter<Product, Long>{

    public ProductCategoryConverter() {
        super(Product.class, Long.class);
    }


    @Override
    public Long convertTo(Product product, Long aLong) {
        if (product == null) return null;
        return product.getSubCategory().getCategory().getId();
    }

    @Override
    public Product convertFrom(Long aLong, Product product) {
        return null;
    }
}
