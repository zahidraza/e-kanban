package com.example.ics.dozer.converter;

import com.example.ics.entity.Product;
import com.example.ics.enums.Role;
import org.dozer.DozerConverter;

public class ProductSubCategoryConverter extends DozerConverter<Product, Long>{

    public ProductSubCategoryConverter() {
        super(Product.class, Long.class);
    }


    @Override
    public Long convertTo(Product product, Long aLong) {
        if (product == null) return null;
        return product.getSubCategory().getId();
    }

    @Override
    public Product convertFrom(Long aLong, Product product) {
        return null;
    }
}
