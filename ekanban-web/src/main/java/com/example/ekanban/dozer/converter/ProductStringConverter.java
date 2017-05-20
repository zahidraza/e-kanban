package com.example.ekanban.dozer.converter;

import com.example.ekanban.entity.Product;
import org.dozer.DozerConverter;

/**
 * Created by mdzahidraza on 20/05/17.
 */
public class ProductStringConverter extends DozerConverter<Product,String> {

    public ProductStringConverter() {
        super(Product.class, String.class);
    }

    @Override
    public String convertTo(Product product, String s) {
        if (product == null) return null;
        return product.getName();
    }

    @Override
    public Product convertFrom(String s, Product product) {
        return null;
    }
}
