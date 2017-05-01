package com.example.ekanban.dto;

import com.example.ekanban.enums.BinState;
import com.example.ekanban.enums.StringEnum;

/**
 * Created by razamd on 4/16/2017.
 */
public class InventoryDto {

    private Long id;

    private Long categoryId;

    private Long subCategoryId;

    private Long productId;

    private Integer binNo;

    @StringEnum(enumClass = BinState.class)
    private String binState;

    private Long lastUpdated;

    public InventoryDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getBinNo() {
        return binNo;
    }

    public void setBinNo(Integer binNo) {
        this.binNo = binNo;
    }

    public String getBinState() {
        return binState;
    }

    public void setBinState(String binState) {
        this.binState = binState;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
