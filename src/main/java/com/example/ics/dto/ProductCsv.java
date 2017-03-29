package com.example.ics.dto;

import com.opencsv.bean.CsvBindByName;

/**
 * Created by razamd on 3/29/2017.
 */
public class ProductCsv {

    @CsvBindByName(column = "PRODUCT_NAME")
    private String name;

    @CsvBindByName(column = "CATEGORY")
    private String category;

    @CsvBindByName(column = "SUB_CATEGORY")
    private String subCategory;

    @CsvBindByName(column = "DESCRIPTION")
    private String description;

    @CsvBindByName(column = "SECTIONS")
    private String sections;

    @CsvBindByName(column = "PRICE")
    private double price;

    @CsvBindByName(column = "ITEM_CODE")
    private String itemCode;

    @CsvBindByName(column = "ORDERING_TIME")
    private Integer timeOrdering;

    @CsvBindByName(column = "PRODUCTION_TIME")
    private Integer timeProcurement;

    @CsvBindByName(column = "TRANSPORTATION_TIME")
    private Integer timeTransporation;

    @CsvBindByName(column = "BUFFER_TIME")
    private Integer timeBuffer;

    @CsvBindByName(column = "UOM_PURCHASE")
    private String uomPurchase;

    @CsvBindByName(column = "UOM_CONSUMPTION")
    private String uomConsumption;

    @CsvBindByName(column = "CONVERSION_FACTOR")
    private double conversionFactor;

    @CsvBindByName(column = "SUPPLIER_NAME")
    private String supplier;

    @CsvBindByName(column = "SUPPLIER_CONTACT_PERSON")
    private String contactPerson;

    @CsvBindByName(column = "SUPPLIER_TYPE")
    private String supplierType;

    @CsvBindByName(column = "MIN_ORDER_QTY")
    private Long minOrderQty;

    @CsvBindByName(column = "PACKET_SIZE")
    private Integer packetSize;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSections() {
        return sections;
    }

    public void setSections(String sections) {
        this.sections = sections;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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

    public double getConversionFactor() {
        return conversionFactor;
    }

    public void setConversionFactor(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
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
}
