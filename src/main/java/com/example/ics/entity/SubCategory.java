/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ics.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author razamd
 */
@Entity
public class SubCategory implements Serializable{
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull @Size(min = 3,max = 255)
    @Column(name = "NAME",nullable = false)
    private String name;
    
    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;
    
    @JsonIgnore
    @OneToMany(mappedBy = "subCategory", fetch = FetchType.LAZY)
    private Set<Product> productList;

    public SubCategory() {
    }

    public SubCategory(Long id) {
        this.id = id;
    }

    
    
    public SubCategory(String name) {
        this.name = name;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<Product> getProductList() {
        return productList;
    }

    public void setProductList(Set<Product> productList) {
        this.productList = productList;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SubCategory other = (SubCategory) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    public String toJsonString(){
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        if(id != null) builder.append("\"id\":" + id + ",\n");
        if(name != null) builder.append("\"name\":\"" + name + "\",\n");
        if(builder.length() > 2){
            builder.setLength(builder.length()-2);
        }
        builder.append("\n}");
        return builder.toString();
    }

    @Override
    public String toString() {
        return "SubCategory{" + "id=" + id + ", name=" + name + '}';
    }
    
}
