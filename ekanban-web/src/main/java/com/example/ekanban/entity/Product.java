/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ekanban.entity;

import com.example.ekanban.enums.ClassType;
import com.example.ekanban.enums.KanbanType;
import com.example.ekanban.respository.ConsumptionRepositoryImpl;
import com.example.ekanban.util.ApplicationContextUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;

/**
 * @author razamd
 */
@Entity
@Table(indexes = @Index(columnList = "sub_category_id"))
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_section",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "section_id")
    )
    private Set<Section> sectionList = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_supplier",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "supplier_id")
    )
    private Set<Supplier> supplierList = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Consumption> consumptions = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Inventory> inventorySet = new HashSet<>();

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = true, length = 500)
    private String description;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "item_code", nullable = false, unique = true)
    private String itemCode;

    @Column(name = "time_ordering", nullable = false)
    private Integer timeOrdering;

    @Column(name = "time_production", nullable = false)
    private Integer timeProduction;

    @Column(name = "time_transportation", nullable = false)
    private Integer timeTransportation;

    @Column(name = "time_buffer", nullable = false)
    private Integer timeBuffer;

    @Column(name = "total_lead_time")
    private Integer totalLeadTime;

    @Column(name = "uom_purchase", nullable = false)
    private String uomPurchase; //Unit of Measurment Purchase

    @Column(name = "uom_consumption", nullable = false)
    private String uomConsumption;

    @Column(name = "conversion_factor", nullable = false)
    private BigDecimal conversionFactor;

    @Column(name = "min_order_qty", nullable = false)
    private Long minOrderQty;

    @Column(name = "packet_size", nullable = false)
    private BigDecimal packetSize;

    @Column(name = "class_type", nullable = true)
    private String classType;

    @Column(name = "kanban_type", nullable = true)
    private String kanbanType;

    @Column(name = "demand", nullable = true)
    private Long demand;

    @Column(name = "no_of_bins", nullable = true)
    private Integer noOfBins;

    @Column(name = "bin_qty", nullable = true)
    private Long binQty;

    @Column(name = "stk_on_floor", nullable = false)
    private Long stkOnFloor;

    @Column(name = "ordered_qty", nullable = false)
    private Long orderedQty;

    @Column(name = "ignore_sync")
    private Boolean ignoreSync;

    @Column(name = "is_new")
    private Boolean isNew;

    @Column(name = "is_freezed")
    private Boolean isFreezed;

    @Column(name = "last_scanned", nullable = false)
    private Date lastScanned;

    @Version
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_updated")
    private Date lastUpdated;

    public Product() {
    }

    public Product(Long id) {
        this.id = id;
    }

    public void addConsumption(Consumption consumption) {
        consumption.setProduct(this);
        consumptions.add(consumption);
    }

    public void addInventory(Inventory inventory) {
        inventory.setProduct(this);
        inventorySet.add(inventory);
    }

    public Set<Inventory> getInventorySet() {
        return inventorySet;
    }

    public Set<Consumption> getConsumptions() {
        ConsumptionRepositoryImpl consumptionRepository = ApplicationContextUtil.getApplicationContext().getBean(ConsumptionRepositoryImpl.class);
        return consumptionRepository.findLastYearConsumption(id);
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

    public Integer getTimeProduction() {
        return timeProduction;
    }

    public void setTimeProduction(Integer timeProduction) {
        this.timeProduction = timeProduction;
    }

    public Integer getTimeTransportation() {
        return timeTransportation;
    }

    public void setTimeTransportation(Integer timeTransportation) {
        this.timeTransportation = timeTransportation;
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

    public BigDecimal getPacketSize() {
        return packetSize;
    }

    public void setPacketSize(BigDecimal packetSize) {
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

    public void setSectionList(Set<Section> sectionList) {
        this.sectionList = sectionList;
    }

    public Set<Supplier> getSupplierList() {
        return supplierList;
    }

    public void setSupplierList(Set<Supplier> supplierList) {
        this.supplierList = supplierList;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public void setKanbanType(String kanbanType) {
        this.kanbanType = kanbanType;
    }

    public Long getStkOnFloor() {
        return stkOnFloor;
    }

    public void setStkOnFloor(Long stkOnFloor) {
        this.stkOnFloor = stkOnFloor;
    }

    public Long getOrderedQty() {
        return orderedQty;
    }

    public void setOrderedQty(Long orderedQty) {
        this.orderedQty = orderedQty;
    }

    public Integer getTotalLeadTime() {
        return totalLeadTime;
    }

    public void setTotalLeadTime(Integer totalLeadTime) {
        this.totalLeadTime = totalLeadTime;
    }

    public Boolean getIgnoreSync() {
        return ignoreSync;
    }

    public void setIgnoreSync(Boolean ignoreSync) {
        this.ignoreSync = ignoreSync;
    }

    public Boolean getNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    public Boolean getFreezed() {
        return isFreezed;
    }

    public void setFreezed(Boolean freezed) {
        isFreezed = freezed;
    }

    public Date getLastScanned() {
        return lastScanned;
    }

    public void setLastScanned(Date lastScanned) {
        this.lastScanned = lastScanned;
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
        return name;
    }
}
