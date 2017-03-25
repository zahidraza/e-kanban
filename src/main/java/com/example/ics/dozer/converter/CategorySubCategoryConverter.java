/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ics.dozer.converter;

import com.example.ics.entity.Category;
import com.example.ics.entity.SubCategory;
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
        Category category = source.getCategory();
        category.setSubCategoryList(null);
        return category;
    }
}
