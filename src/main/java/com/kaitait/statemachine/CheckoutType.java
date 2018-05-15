package com.kaitait.statemachine;

import java.util.Arrays;

public enum CheckoutType {
    DELIVERY("delivery"),
    PICKUP("pickup");

    private final String type;

    CheckoutType(String type) {
        this.type = type;
    }

    public static CheckoutType getByType(String type) {
        return Arrays.stream(values())
                .filter(bl -> bl.type.equalsIgnoreCase(type))
                .findFirst()
                .orElse(PICKUP);
    }
}
