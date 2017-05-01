/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ekanban.enums;

/**
 *
 * @author razamd
 */
public enum SupplierType {
    LOCAL("LOCAL"), NON_LOCAL("NON_LOCAL");

    private final String value;

    private SupplierType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SupplierType parse(String value) {
        SupplierType supplierType = null;
        for (SupplierType item : SupplierType.values()) {
            if (item.getValue().equals(value)) {
                supplierType = item;
                break;
            }
        }
        return supplierType;
    }
}
