package com.example.ekanban.dto;

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
    private Double price;

    @CsvBindByName(column = "ITEM_CODE")
    private String itemCode;

    @CsvBindByName(column = "ORDERING_TIME")
    private Double timeOrdering;

    @CsvBindByName(column = "PRODUCTION_TIME")
    private Double timeProcurement;

    @CsvBindByName(column = "TRANSPORTATION_TIME")
    private Double timeTransporation;

    @CsvBindByName(column = "BUFFER_TIME")
    private Double timeBuffer;

    @CsvBindByName(column = "UOM_PURCHASE")
    private String uomPurchase;

    @CsvBindByName(column = "UOM_CONSUMPTION")
    private String uomConsumption;

    @CsvBindByName(column = "CONVERSION_FACTOR")
    private Double conversionFactor;

    @CsvBindByName(column = "SUPPLIER_NAME")
    private String supplier;

    @CsvBindByName(column = "SUPPLIER_CONTACT_PERSON")
    private String contactPerson;

    @CsvBindByName(column = "SUPPLIER_TYPE")
    private String supplierType;

    @CsvBindByName(column = "MIN_ORDER_QTY")
    private Double minOrderQty;

    @CsvBindByName(column = "PACKET_SIZE")
    private Double packetSize;

    @CsvBindByName(column = "STOCK_ON_FLOOR")
    private Double stkOnFloor;

    @CsvBindByName(column = "ORDERED_QTY")
    private Double orderedQty;

    @CsvBindByName(column = "JAN")
    private Double jan;

    @CsvBindByName(column = "FEB")
    private Double feb;

    @CsvBindByName(column = "MAR")
    private Double mar;

    @CsvBindByName(column = "APR")
    private Double apr;

    @CsvBindByName(column = "MAY")
    private Double may;

    @CsvBindByName(column = "JUN")
    private Double jun;

    @CsvBindByName(column = "JUL")
    private Double jul;

    @CsvBindByName(column = "AUG")
    private Double aug;

    @CsvBindByName(column = "SEP")
    private Double sep;

    @CsvBindByName(column = "OCT")
    private Double oct;

    @CsvBindByName(column = "NOV")
    private Double nov;

    @CsvBindByName(column = "DEC")
    private Double dec;

    public ProductCsv() {
    }

    public ProductCsv(String name, String category, String subCategory, String description, String sections, Double
            price, String itemCode, Double timeOrdering, Double timeProcurement, Double timeTransporation, Double
            timeBuffer, String uomPurchase, String uomConsumption, Double conversionFactor, String supplier, String
            contactPerson, String supplierType, Double minOrderQty, Double packetSize) {
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Double getTimeOrdering() {
        return timeOrdering;
    }

    public void setTimeOrdering(Double timeOrdering) {
        this.timeOrdering = timeOrdering;
    }

    public Double getTimeProcurement() {
        return timeProcurement;
    }

    public void setTimeProcurement(Double timeProcurement) {
        this.timeProcurement = timeProcurement;
    }

    public Double getTimeTransporation() {
        return timeTransporation;
    }

    public void setTimeTransporation(Double timeTransporation) {
        this.timeTransporation = timeTransporation;
    }

    public Double getTimeBuffer() {
        return timeBuffer;
    }

    public void setTimeBuffer(Double timeBuffer) {
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

    public Double getConversionFactor() {
        return conversionFactor;
    }

    public void setConversionFactor(Double conversionFactor) {
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

    public Double getMinOrderQty() {
        return minOrderQty;
    }

    public void setMinOrderQty(Double minOrderQty) {
        this.minOrderQty = minOrderQty;
    }

    public Double getPacketSize() {
        return packetSize;
    }

    public void setPacketSize(Double packetSize) {
        this.packetSize = packetSize;
    }

    public Double getJan() {
        return jan;
    }

    public void setJan(Double jan) {
        this.jan = jan;
    }

    public Double getFeb() {
        return feb;
    }

    public void setFeb(Double feb) {
        this.feb = feb;
    }

    public Double getMar() {
        return mar;
    }

    public void setMar(Double mar) {
        this.mar = mar;
    }

    public Double getApr() {
        return apr;
    }

    public void setApr(Double apr) {
        this.apr = apr;
    }

    public Double getMay() {
        return may;
    }

    public void setMay(Double may) {
        this.may = may;
    }

    public Double getJun() {
        return jun;
    }

    public void setJun(Double jun) {
        this.jun = jun;
    }

    public Double getJul() {
        return jul;
    }

    public void setJul(Double jul) {
        this.jul = jul;
    }

    public Double getAug() {
        return aug;
    }

    public void setAug(Double aug) {
        this.aug = aug;
    }

    public Double getSep() {
        return sep;
    }

    public void setSep(Double sep) {
        this.sep = sep;
    }

    public Double getOct() {
        return oct;
    }

    public void setOct(Double oct) {
        this.oct = oct;
    }

    public Double getNov() {
        return nov;
    }

    public void setNov(Double nov) {
        this.nov = nov;
    }

    public Double getDec() {
        return dec;
    }

    public void setDec(Double dec) {
        this.dec = dec;
    }

    public Double getStkOnFloor() {
        return stkOnFloor;
    }

    public void setStkOnFloor(Double stkOnFloor) {
        this.stkOnFloor = stkOnFloor;
    }

    public Double getOrderedQty() {
        return orderedQty;
    }

    public void setOrderedQty(Double orderedQty) {
        this.orderedQty = orderedQty;
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
                ", stkOnFloor=" + stkOnFloor +
                ", orderedQty=" + orderedQty +
                ", jan=" + jan +
                ", feb=" + feb +
                ", mar=" + mar +
                ", apr=" + apr +
                ", may=" + may +
                ", jun=" + jun +
                ", jul=" + jul +
                ", aug=" + aug +
                ", sep=" + sep +
                ", oct=" + oct +
                ", nov=" + nov +
                ", dec=" + dec +
                '}';
    }
}
