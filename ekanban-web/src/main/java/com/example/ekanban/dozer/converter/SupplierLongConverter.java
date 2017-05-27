package com.example.ekanban.dozer.converter;

import com.example.ekanban.entity.Supplier;
import org.dozer.DozerConverter;

/**
 * Created by mdzahidraza on 27/05/17.
 */
public class SupplierLongConverter extends DozerConverter<Supplier,Long> {

    public SupplierLongConverter() {
        super(Supplier.class, Long.class);
    }

    @Override
    public Long convertTo(Supplier supplier, Long aLong) {
        return supplier == null ? null: supplier.getId();
    }

    @Override
    public Supplier convertFrom(Long aLong, Supplier supplier) {
        return aLong == null ? null : new Supplier(aLong);
    }
}
