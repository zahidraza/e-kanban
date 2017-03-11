/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.ics.enums;

/**
 *
 * @author razamd
 */
public enum ClassType {
    CLASS_A("CLASS_A"), CLASS_B("CLASS_B"), CLASS_C("CLASS_C");

    private String value;

    private ClassType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ClassType parse(String value) {
        ClassType classType = null;
        for (ClassType item : ClassType.values()) {
            if (item.getValue().equals(value)) {
                classType = item;
                break;
            }
        }
        return classType;
    }
}
