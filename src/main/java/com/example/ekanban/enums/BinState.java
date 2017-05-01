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
public enum BinState {
    UNAVAILABLE("UNAVAILABLE"), STORE("STORE"), PURCHASE("PURCHASE"), ORDERED("ORDERED");

    private final String value;

    BinState(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static BinState parse(String value) {
        BinState binState = null;
        for (BinState item : BinState.values()) {
            if (item.getValue().equals(value)) {
                binState = item;
                break;
            }
        }
        return binState;
    }
}
