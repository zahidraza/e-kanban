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
public enum OrderState {
    NEW("NEW"),ORDERED("ORDERED"),  COMPLETED("COMPLETED");

    private final String value;

    OrderState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OrderState parse(String value) {
        OrderState orderState = null;
        for (OrderState item : OrderState.values()) {
            if (item.getValue().equals(value)) {
                orderState = item;
                break;
            }
        }
        return orderState;
    }
}
