/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ekanban.entity;

import com.example.ekanban.enums.OrderState;
import com.example.ekanban.enums.StringEnum;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author razamd
 */
@Entity
@Table(name = "ORDERS",indexes = @Index(columnList = "PRODUCT_ID"))
public class Order implements Serializable{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    @Column(name = "BINS", nullable = false)
    private String bins;

    @Column(name = "ORDERED_AT", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderedAt;
    
    @Column(name = "COMPLETED_AT", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedAt;

    @ManyToOne(optional = true)
    @JoinColumn(name = "ORDERED_BY")
    private User orderedBy;

    @JoinColumn(name = "ORDER_STATE")
    private String orderState;

    @Version
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_UPDATED")
    private Date lastUpdated;

    public Order() {
    }

    public Order(Product product, String bins, Date orderedAt, User orderedBy, String orderState) {
        this.product = product;
        this.bins = bins;
        this.orderedAt = orderedAt;
        this.orderedBy = orderedBy;
        this.orderState = orderState;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(Date orderedAt) {
        this.orderedAt = orderedAt;
    }

    public User getOrderedBy() {
        return orderedBy;
    }

    public void setOrderedBy(User orderedBy) {
        this.orderedBy = orderedBy;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public OrderState getOrderState() {
        return OrderState.parse(orderState);
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState.getValue();
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getBins() {
        return bins;
    }

    public void setBins(String bins) {
        this.bins = bins;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.id);
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
        final Order other = (Order) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
