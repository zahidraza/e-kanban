/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ics.entity;

import java.io.Serializable;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author razamd
 */
@Embeddable @Access(AccessType.FIELD)
public class Address implements Serializable{
    
    @Column(name = "STREET")
    @NotEmpty
    private String street;
    
    @Column(name = "LANDMARK")
    private String landmark;
    
    @Column(name = "CITY")
    @NotEmpty
    private String city;
    
    @Column(name = "STATE")
    @NotEmpty
    private String state;
    
    @Column(name = "ZIP_CODE")
    @NotNull @Pattern(regexp="[0-9]{6}")
    private String zip;
    
    @Column(name = "COUNTRY")
    @NotEmpty
    private String country;

    public Address() {
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Address{" +
               "street='" + street + '\'' +
               ", landmark='" + landmark + '\'' +
               ", city='" + city + '\'' +
               ", state='" + state + '\'' +
               ", zip='" + zip + '\'' +
               ", country='" + country + '\'' +
               '}';
    }
}
