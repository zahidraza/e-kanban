/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ics.entity;

import com.example.ics.enums.ClassType;
import com.example.ics.enums.KanbanType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 *
 * @author razamd
 */
@Entity
public class Product {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "SUB_CATEGORY_ID")
    private SubCategory subCategory;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "PRODUCT_SECTION",
            joinColumns = @JoinColumn(name = "PRODUCT_ID"),
            inverseJoinColumns = @JoinColumn(name = "SECTION_ID")
    )
    private Set<Section> sectionList = new HashSet<>();
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "PRODUCT_SUPPLIER",
            joinColumns = @JoinColumn(name = "PRODUCT_ID"),
            inverseJoinColumns = @JoinColumn(name = "SUPPLIER_ID")
    )
    private Set<Supplier> supplierList = new HashSet<>();
    
    @Column(name = "NAME",nullable = false)
    private String name;
    
    @Column(name = "DESCRIPTION",nullable = true,length = 500)
    private String description;
    
    @Column(name = "PRICE",nullable = false)
    private BigDecimal price;
    
    @Column(name = "ITEM_CODE",nullable = true)
    private String itemCode;
    
    @Column(name = "TIME_ORDERING",nullable = false)
    private Integer timeOrdering;
    
    @Column(name = "TIME_PROCUREMENT",nullable = false)
    private Integer timeProcurement;
    
    @Column(name = "TIME_TRANSPORTION",nullable = false)
    private Integer timeTransporation;
    
    @Column(name = "TIME_BUFFER",nullable = false) 
    private Integer timeBuffer;
    
    @Column(name = "UOM_PURCHASE",nullable = false)
    private String uomPurchase; //Unit of Measurment Purchase
    
    @Column(name = "UOM_CONSUMPTION",nullable = false)
    private String uomConsumption;
    
    @Column(name = "CONVERSION_FACTOR",nullable = false)
    private BigDecimal conversionFactor;
    
    @Column(name = "MIN_ORDER_QTY",nullable = false)
    private Long minOrderQty;
    
    @Column(name = "PACKET_SIZE",nullable = false)
    private Integer packetSize;
    
    @Column(name = "CLASS_TYPE",nullable = true)
    private String classType;
    
    @Column(name = "KANBAN_TYPE",nullable = true)
    private String kanbanType;
    
    @Column(name = "DEMAND",nullable = true)
    private Long demand;
    
    @Column(name = "NO_OF_BINS",nullable = true)
    private Integer noOfBins;
    
    @Column(name = "BIN_QTY",nullable = true)
    private Long binQty;

    @Version
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_UPDATED")
    private Date lastUpdated;

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Integer getTimeOrdering() {
        return timeOrdering;
    }

    public void setTimeOrdering(Integer timeOrdering) {
        this.timeOrdering = timeOrdering;
    }

    public Integer getTimeProcurement() {
        return timeProcurement;
    }

    public void setTimeProcurement(Integer timeProcurement) {
        this.timeProcurement = timeProcurement;
    }

    public Integer getTimeTransporation() {
        return timeTransporation;
    }

    public void setTimeTransporation(Integer timeTransporation) {
        this.timeTransporation = timeTransporation;
    }

    public Integer getTimeBuffer() {
        return timeBuffer;
    }

    public void setTimeBuffer(Integer timeBuffer) {
        this.timeBuffer = timeBuffer;
    }

    public String getUomPurchase() {
        return uomPurchase;
    }

    public void setUomPurchase(String uomPurchase) {
        this.uomPurchase = uomPurchase;
    }

    public String getUomConsumption() {
        return uomConsumption;
    }

    public void setUomConsumption(String uomConsumption) {
        this.uomConsumption = uomConsumption;
    }

    public BigDecimal getConversionFactor() {
        return conversionFactor;
    }

    public void setConversionFactor(BigDecimal conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public Long getMinOrderQty() {
        return minOrderQty;
    }

    public void setMinOrderQty(Long minOrderQty) {
        this.minOrderQty = minOrderQty;
    }

    public Integer getPacketSize() {
        return packetSize;
    }

    public void setPacketSize(Integer packetSize) {
        this.packetSize = packetSize;
    }

    public ClassType getClassType() {
        return ClassType.parse(classType);
    }

    public void setClassType(ClassType classType) {
        this.classType = classType.getValue();
    }

    public KanbanType getKanbanType() {
        return KanbanType.parse(kanbanType);
    }

    public void setKanbanType(KanbanType kanbanType) {
        this.kanbanType = kanbanType.getValue();
    }

    public Long getDemand() {
        return demand;
    }

    public void setDemand(Long demand) {
        this.demand = demand;
    }

    public Integer getNoOfBins() {
        return noOfBins;
    }

    public void setNoOfBins(Integer noOfBins) {
        this.noOfBins = noOfBins;
    }

    public Long getBinQty() {
        return binQty;
    }

    public void setBinQty(Long binQty) {
        this.binQty = binQty;
    }

    public Set<Section> getSectionList() {
        return sectionList;
    }

//    public void setSectionList(Set<Section> sectionList) {
//        this.sectionList = sectionList;
//    }

    public Set<Supplier> getSupplierList() {
        return supplierList;
    }
//
//    public void setSupplierList(Set<Supplier> supplierList) {
//        this.supplierList = supplierList;
//    }


    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.id);
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
        final Product other = (Product) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
       
//    public String toJsonString(){
//        StringBuilder builder = new StringBuilder();
//        builder.append("{\n");
//        if(id != null) builder.append("\"id\":" + id + ",\n");
//        if(name != null) builder.append("\"name\":\"" + name + "\",\n");
//        if(email != null) builder.append("\"email\":\"" + email + "\",\n");
//        if(role != null) builder.append("\"role\":\"" + role + "\",\n");
//        if(mobile != null) builder.append("\"mobile\":\"" + mobile + "\",");
//        
//        if(builder.length() > 2){
//            builder.setLength(builder.length()-1);
//        }
//        builder.append("\n}");
//        return builder.toString();
//    }

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", subCategory=" + subCategory + ", name=" + name + ", description=" + description + ", price=" + price + ", itemCode=" + itemCode + ", timeOrdering=" + timeOrdering + ", timeProcurement=" + timeProcurement + ", timeTransporation=" + timeTransporation + ", timeBuffer=" + timeBuffer + ", uomPurchase=" + uomPurchase + ", uomConsumption=" + uomConsumption + ", conversionFactor=" + conversionFactor + ", minOrderQty=" + minOrderQty + ", packetSize=" + packetSize + ", classType=" + classType + ", kanbanType=" + kanbanType + ", demand=" + demand + ", noOfBins=" + noOfBins + ", binQty=" + binQty + '}';
    }
}
