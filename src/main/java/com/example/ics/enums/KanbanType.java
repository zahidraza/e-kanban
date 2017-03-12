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
public enum KanbanType {
    N_BIN("N_BIN"), TWO_BIN("TWO_BIN");

    private final String value;

    private KanbanType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static KanbanType parse(String value) {
        KanbanType kanbanType = null;
        for (KanbanType item : KanbanType.values()) {
            if (item.getValue().equals(value)) {
                kanbanType = item;
                break;
            }
        }
        return kanbanType;
    }
}
