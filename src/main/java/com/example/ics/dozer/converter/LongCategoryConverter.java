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
public class LongCategoryConverter extends DozerConverter<Long, SubCategory>{
    public LongCategoryConverter() {
        super(Long.class, SubCategory.class);
    }

    @Override
    public SubCategory convertTo(Long source, SubCategory destination) {
        return null;
    }

    @Override
    public Long convertFrom(SubCategory source, Long destination) {
        if(source == null) return null;
        return source.getCategory().getId();
    }
}
