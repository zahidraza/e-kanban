package com.example.ics.entity;

import com.example.ics.enums.StringEnum;
import com.example.ics.enums.SupplierType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Supplier implements Serializable{
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull @Size(min = 2, max = 255) 
    @Column(name = "NAME", nullable = false, unique = true)
    private String name;
    
    @NotEmpty
    @Column(name = "CONTACT_PERSON")
    private String contactPerson;
    
    @NotNull @StringEnum(enumClass = SupplierType.class)
    @Column(name = "SUPPLIER_TYPE")
    private String supplierType;   //Local | NonLocal
    
    @Embedded
    @Valid
    private Address address;

    @JsonIgnore
    @ManyToMany(mappedBy = "supplierList")
    private Set<Product> productList;

    @Version
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_UPDATED")
    private Date lastUpdated;
    
    public Supplier() {
    }

    public Supplier(String name, String contactPerson, String supplierType) {
        this.name = name;
        this.contactPerson = contactPerson;
        this.supplierType = supplierType;
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

    public Set<Product> getProductList() {
        return productList;
    }

    public void setProductList(Set<Product> productList) {
        this.productList = productList;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String toJsonString(){
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        if(id != null) builder.append("\"id\":" + id + ",\n");
        if(name != null) builder.append("\"name\":\"" + name + "\",\n");
        if(contactPerson != null) builder.append("\"contactPerson\":\"" + contactPerson + "\",\n");
        if(supplierType != null) builder.append("\"supplierType\":\"" + supplierType + "\",\n");
        if(address != null){
            builder.append("\"address\":{\n");
            if(address.getStreet() != null) builder.append("\"street\":\"" + address.getStreet() + "\",\n");
            if(address.getLandmark() != null) builder.append("\"landmark\":\"" + address.getLandmark() + "\",\n");
            if(address.getCity() != null) builder.append("\"city\":\"" + address.getCity() + "\",\n");
            if(address.getState() != null) builder.append("\"state\":\"" + address.getState() + "\",\n");
            if(address.getCountry() != null) builder.append("\"country\":\"" + address.getCountry() + "\",\n");
            if(address.getZip() != null) builder.append("\"zip\":\"" + address.getZip() + "\",\n");
            if(address.getStreet() != null || address.getLandmark() != null || address.getCity() != null || address.getState() != null || address.getCountry() != null || address.getZip() != null){
                builder.setLength(builder.length()-2);
            }
            builder.append("\n}");
        }
        if(builder.length() > 2){
            builder.setLength(builder.length()-2);
        }
        builder.append("\n}");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.id);
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
        final Supplier other = (Supplier) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Supplier{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", contactPerson='" + contactPerson + '\'' +
               ", supplierType='" + supplierType + '\'' +
               ", address=" + address +
               '}';
    }
}
