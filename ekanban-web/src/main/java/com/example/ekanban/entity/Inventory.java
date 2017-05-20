/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ekanban.entity;

import com.example.ekanban.enums.BinState;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author razamd
 */
@Entity
@Table(indexes = @Index(columnList = "PRODUCT_ID"))
public class Inventory implements Serializable{
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;
    
    @Column(name = "BIN_NO", nullable = false)
    private Integer binNo;
    
    @Column(name = "BIN_STATE", nullable = false)
    private String binState;

    @Version
    @Column(name = "LAST_UPDATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    public Inventory() {
    }

    public Inventory(Integer binNo, BinState binState) {
        this.binNo = binNo;
        this.binState = binState.getValue();
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

    public Integer getBinNo() {
        return binNo;
    }

    public void setBinNo(Integer binNo) {
        this.binNo = binNo;
    }

    public BinState getBinState() {
        return BinState.parse(binState);
    }

    public void setBinState(BinState binState) {
        this.binState = binState.getValue();
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

        Inventory inventory = (Inventory) o;

        if (!product.equals(inventory.product)) return false;
        return binNo.equals(inventory.binNo);
    }

    @Override
    public int hashCode() {
        int result = product.hashCode();
        result = 31 * result + binNo.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", product=" + product +
                ", binNo=" + binNo +
                ", binState='" + binState + '\'' +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
