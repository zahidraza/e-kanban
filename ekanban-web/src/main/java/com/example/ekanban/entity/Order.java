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
@Table(name = "orders",indexes = @Index(columnList = "product_id"))
public class Order implements Serializable{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(optional = true)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Column(name = "bins", nullable = false)
    private String bins;

    @Column(name = "bins_scanned")
    private String binsScanned;

    @Column(name = "bin_qty")
    private Long binQty;

    @Column(name = "ordered_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderedAt;
    
    @Column(name = "completed_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ordered_by")
    private User orderedBy;

    @JoinColumn(name = "order_state")
    private String orderState;

    @Column(name = "is_followed_up")
    private Boolean isFollowedUp;

    @Version
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_updated")
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

//    public Order(Product product, Supplier supplier, String bins, Date orderedAt, User orderedBy, String orderState) {
//        this.product = product;
//        this.supplier = supplier;
//        this.bins = bins;
//        this.orderedAt = orderedAt;
//        this.orderedBy = orderedBy;
//        this.orderState = orderState;
//    }

    public Order(Product product, Supplier supplier, String bins, Long binQty, Date orderedAt, User orderedBy, String orderState) {
        this.product = product;
        this.supplier = supplier;
        this.bins = bins;
        this.binQty = binQty;
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

    public Boolean getFollowedUp() {
        return isFollowedUp;
    }

    public void setFollowedUp(Boolean followedUp) {
        isFollowedUp = followedUp;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getBinsScanned() {
        return binsScanned;
    }

    public void setBinsScanned(String binsScanned) {
        this.binsScanned = binsScanned;
    }

    public Long getBinQty() {
        return binQty;
    }

    public void setBinQty(Long binQty) {
        this.binQty = binQty;
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
