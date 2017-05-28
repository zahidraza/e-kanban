package com.example.ekanban.dto;

import com.example.ekanban.enums.OrderState;
import com.example.ekanban.enums.StringEnum;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by razamd on 5/2/2017.
 */
public class OrderDto {

    private Long id;

    //@Digits(integer = 5,fraction = 0)
    //@NotNull
    private Long productId;

    //@NotNull
    @Pattern(regexp="^([0-9]+)|([0-9]+[0-9,]*[0-9]+)$")  //Regex for comma separated bins
    private String bins;

    @Pattern(regexp="^([0-9]+)|([0-9]+[0-9,]*[0-9]+)$")  //Regex for comma separated bins
    private String binsScanned;

    //@NotNull
    private Long binQty;

    private Long supplierId;

    @Digits(integer = 14,fraction = 0)
    private Long orderedAt;

    @Digits(integer = 14,fraction = 0)
    private Long completedAt;

    //@NotNull
    @Digits(integer = 5,fraction = 0)
    private Long orderedBy;

    @StringEnum(enumClass = OrderState.class)
    private String orderState;

    private Boolean isFollowedUp;

    private Long lastUpdated;

    public OrderDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getBins() {
        return bins;
    }

    public void setBins(String bins) {
        this.bins = bins;
    }

    public Long getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(Long orderedAt) {
        this.orderedAt = orderedAt;
    }

    public Long getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Long completedAt) {
        this.completedAt = completedAt;
    }

    public Long getOrderedBy() {
        return orderedBy;
    }

    public void setOrderedBy(Long orderedBy) {
        this.orderedBy = orderedBy;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Boolean getFollowedUp() {
        return isFollowedUp;
    }

    public void setFollowedUp(Boolean followedUp) {
        isFollowedUp = followedUp;
    }

    public String getBinsScanned() {
        return binsScanned;
    }

    public void setBinsScanned(String binsScanned) {
        this.binsScanned = binsScanned;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getBinQty() {
        return binQty;
    }

    public void setBinQty(Long binQty) {
        this.binQty = binQty;
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "id=" + id +
                ", productId=" + productId +
                ", bins='" + bins + '\'' +
                ", binsScanned='" + binsScanned + '\'' +
                ", supplierId=" + supplierId +
                ", orderedAt=" + orderedAt +
                ", completedAt=" + completedAt +
                ", orderedBy=" + orderedBy +
                ", orderState='" + orderState + '\'' +
                ", isFollowedUp=" + isFollowedUp +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
