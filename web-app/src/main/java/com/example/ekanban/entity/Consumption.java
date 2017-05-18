/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ekanban.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author razamd
 */

@Entity
@Table(indexes = @Index(columnList = "PRODUCT_ID"))
public class Consumption {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;
    
    @Column(name = "YEAR")
    private Integer year;
    
    @Column(name = "MONTH")
    private Integer month;
    
    @Column(name = "VALUE")
    private Long value;

    @Version
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_UPDATED")
    private Date lastUpdated;

    public Consumption() {
    }

    public Consumption(Integer year, Integer month, Long value) {
        this.year = year;
        this.month = month;
        this.value = value;
    }

    public Consumption(Long id, Product product, Integer year, Integer month, Long value) {
        this.id = id;
        this.product = product;
        this.year = year;
        this.month = month;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Consumption that = (Consumption) o;

        if (!year.equals(that.year)) return false;
        if (!month.equals(that.month)) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        int result = year.hashCode();
        result = 31 * result + month.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Consumption{" +
                "year=" + year +
                ", month=" + month +
                ", value=" + value +
                '}';
    }
}
