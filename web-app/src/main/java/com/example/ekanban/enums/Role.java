package com.example.ekanban.enums;

public enum Role {
    ADMIN("ROLE_ADMIN"), STORE("ROLE_STORE"),PURCHASE("ROLE_PURCHASE"), USER("ROLE_USER");

    private final String value;

    private Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Role parse(String value) {
        Role role = null;
        for (Role item : Role.values()) {
            if (item.getValue().equals(value)) {
                role = item;
                break;
            }
        }
        return role;
    }
}
