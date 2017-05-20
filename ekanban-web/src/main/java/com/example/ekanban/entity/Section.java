/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ekanban.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 *
 * @author razamd
 */
@Entity
public class Section {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotEmpty
    @Column(name = "NAME", nullable = false, unique = true)
    private String name;
    
    @JsonIgnore
    @ManyToMany(mappedBy = "sectionList")
    private Set<Product> productList;

    @Version
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_UPDATED")
    private Date lastUpdated;

    public Section() {
    }

    public Section(String name) {
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

    public Set<Product> getProductList() {
        return productList;
    }

    public void setProductList(Set<Product> productList) {
        this.productList = productList;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
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
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final Section other = (Section) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
}
