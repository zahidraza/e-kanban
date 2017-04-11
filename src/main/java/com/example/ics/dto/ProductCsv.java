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
    private double timeOrdering;

    @CsvBindByName(column = "PRODUCTION_TIME")
    private double timeProcurement;

    @CsvBindByName(column = "TRANSPORTATION_TIME")
    private double timeTransporation;

    @CsvBindByName(column = "BUFFER_TIME")
    private double timeBuffer;

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
    private double minOrderQty;

    @CsvBindByName(column = "PACKET_SIZE")
    private double packetSize;

    public ProductCsv() {
    }

    public ProductCsv(String name, String category, String subCategory, String description, String sections, double price, String itemCode, Integer timeOrdering, Integer timeProcurement, Integer timeTransporation, Integer timeBuffer, String uomPurchase, String uomConsumption, double conversionFactor, String supplier, String contactPerson, String supplierType, Long minOrderQty, Integer packetSize) {
        this.name = name;
        this.category = category;
        this.subCategory = subCategory;
        this.description = description;
        this.sections = sections;
        this.price = price;
        this.itemCode = itemCode;
        this.timeOrdering = timeOrdering;
        this.timeProcurement = timeProcurement;
        this.timeTransporation = timeTransporation;
        this.timeBuffer = timeBuffer;
        this.uomPurchase = uomPurchase;
        this.uomConsumption = uomConsumption;
        this.conversionFactor = conversionFactor;
        this.supplier = supplier;
        this.contactPerson = contactPerson;
        this.supplierType = supplierType;
        this.minOrderQty = minOrderQty;
        this.packetSize = packetSize;
    }

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

    public double getTimeOrdering() {
        return timeOrdering;
    }

    public void setTimeOrdering(double timeOrdering) {
        this.timeOrdering = timeOrdering;
    }

    public double getTimeProcurement() {
        return timeProcurement;
    }

    public void setTimeProcurement(double timeProcurement) {
        this.timeProcurement = timeProcurement;
    }

    public double getTimeTransporation() {
        return timeTransporation;
    }

    public void setTimeTransporation(double timeTransporation) {
        this.timeTransporation = timeTransporation;
    }

    public double getTimeBuffer() {
        return timeBuffer;
    }

    public void setTimeBuffer(double timeBuffer) {
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

    public double getMinOrderQty() {
        return minOrderQty;
    }

    public void setMinOrderQty(double minOrderQty) {
        this.minOrderQty = minOrderQty;
    }

    public double getPacketSize() {
        return packetSize;
    }

    public void setPacketSize(double packetSize) {
        this.packetSize = packetSize;
    }

    @Override
    public String toString() {
        return "ProductCsv{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", subCategory='" + subCategory + '\'' +
                ", description='" + description + '\'' +
                ", sections='" + sections + '\'' +
                ", price=" + price +
                ", itemCode='" + itemCode + '\'' +
                ", timeOrdering=" + timeOrdering +
                ", timeProcurement=" + timeProcurement +
                ", timeTransporation=" + timeTransporation +
                ", timeBuffer=" + timeBuffer +
                ", uomPurchase='" + uomPurchase + '\'' +
                ", uomConsumption='" + uomConsumption + '\'' +
                ", conversionFactor=" + conversionFactor +
                ", supplier='" + supplier + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", supplierType='" + supplierType + '\'' +
                ", minOrderQty=" + minOrderQty +
                ", packetSize=" + packetSize +
                '}';
    }
}
