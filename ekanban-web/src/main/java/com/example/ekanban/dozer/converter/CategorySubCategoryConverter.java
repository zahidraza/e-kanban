/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ekanban.dozer.converter;

import com.example.ekanban.entity.Category;
import com.example.ekanban.entity.SubCategory;
import org.dozer.DozerConverter;

/**
 *
 * @author razamd
 */
public class CategorySubCategoryConverter extends DozerConverter<Category, SubCategory>{
    public CategorySubCategoryConverter() {
        super(Category.class, SubCategory.class);
    }

    @Override
    public SubCategory convertTo(Category source, SubCategory destination) {
        return null;
    }

    @Override
    public Category convertFrom(SubCategory source, Category destination) {
        if(source == null) return null;
        source.setProductList(null);
        Category category = source.getCategory();
        category.setSubCategoryList(null);
        return category;
    }
}
