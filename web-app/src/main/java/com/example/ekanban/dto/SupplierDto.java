package com.example.ekanban.dto;

import com.example.ekanban.entity.Address;
import com.example.ekanban.entity.Product;
import com.example.ekanban.enums.StringEnum;
import com.example.ekanban.enums.SupplierType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by mdzahidraza on 20/05/17.
 */
public class SupplierDto {

    private Long id;

    @NotEmpty
    private String name;

    private String contactPerson;

    @StringEnum(enumClass = SupplierType.class)
    private String supplierType;

    private List<String> products;

    @Valid
    private Address address;

    private Date lastUpdated;

    public SupplierDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
