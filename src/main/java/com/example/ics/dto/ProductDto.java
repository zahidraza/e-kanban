package com.example.ics.dto;

import com.example.ics.entity.Category;
import com.example.ics.entity.Section;
import com.example.ics.entity.SubCategory;
import com.example.ics.entity.Supplier;
import com.example.ics.enums.ClassType;
import com.example.ics.enums.KanbanType;
import com.example.ics.enums.StringEnum;
import com.example.ics.util.MiscUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.hibernate.validator.constraints.NotEmpty;

public class ProductDto {
    
    private Category category;
    
    private SubCategory subCategory;
    
    private Long id;
    
    @NotNull @Size(min = 3,max = 255)
    private String name;
    
    @Size(max = 500)
    private String description;
    
    @NotNull @Digits(integer = 10,fraction = 2)
    private BigDecimal price;
     
    @Size(max = 255)
    private String itemCode;
    
    @NotNull 
    private Integer timeOrdering;
    
    @NotNull
    private Integer timeProcurement;
    
    @NotNull
    private Integer timeTransporation;
    
    @NotNull
    private Integer timeBuffer;
    
    @NotEmpty
    private String uomPurchase; //Unit of Measurment Purchase
    
    @NotEmpty
    private String uomConsumption;
    
    @NotNull
    @Digits(integer = 3,fraction = 3)
    private BigDecimal conversionFactor;
    
    @NotNull
    private Long minOrderQty;
    
    @NotNull
    private Integer packetSize;
    
    @StringEnum(enumClass = ClassType.class)
    private String classType;
    
    @StringEnum(enumClass = KanbanType.class)
    private String kanbanType;
    
    @Digits(integer = 10,fraction = 0)
    private Long demand;
    
    @Max(value = 1000)
    private Integer noOfBins;
    
    @Digits(integer = 10,fraction = 0)
    private Long binQty;

    private Date lastUpdated;

    @JsonInclude(Include.NON_NULL)
    private List<String> sections;

    @JsonInclude(Include.NON_NULL)
    private List<String> suppliers;
    
    private List<Section> sectionList ;
    
    private List<Supplier> supplierList;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
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

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getKanbanType() {
        return kanbanType;
    }

    public void setKanbanType(String kanbanType) {
        this.kanbanType = kanbanType;
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

    public List<Section> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    public List<Supplier> getSupplierList() {
        return supplierList;
    }

    public void setSupplierList(List<Supplier> supplierList) {
        this.supplierList = supplierList;
    }

    public List<String> getSections() {
        return sections;
    }

    public void setSections(List<String> sections) {
        this.sections = sections;
    }

    public List<String> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<String> suppliers) {
        this.suppliers = suppliers;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "ProductDto{" + "category=" + category + ", subCategory=" + subCategory + ", id=" + id + ", name=" + name + ", description=" + description + ", price=" + price + ", itemCode=" + itemCode + ", timeOrdering=" + timeOrdering + ", timeProcurement=" + timeProcurement + ", timeTransporation=" + timeTransporation + ", timeBuffer=" + timeBuffer + ", uomPurchase=" + uomPurchase + ", uomConsumption=" + uomConsumption + ", conversionFactor=" + conversionFactor + ", minOrderQty=" + minOrderQty + ", packetSize=" + packetSize + ", classType=" + classType + ", kanbanType=" + kanbanType + ", demand=" + demand + ", noOfBins=" + noOfBins + ", binQty=" + binQty + '}';
    }

}
