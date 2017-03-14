package com.example.ics.dozer.converter;

import com.example.ics.entity.SubCategory;
import org.dozer.DozerConverter;

public class LongSubCategoryConverter extends DozerConverter<Long, SubCategory>{
    public LongSubCategoryConverter() {
        super(Long.class, SubCategory.class);
    }

    @Override
    public SubCategory convertTo(Long source, SubCategory destination) {
        return null;
    }

    @Override
    public Long convertFrom(SubCategory source, Long destination) {
        if(source == null) return null;
        return source.getId();
    }
}
