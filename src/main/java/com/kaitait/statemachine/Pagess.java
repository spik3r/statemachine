package com.kaitait.statemachine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public enum Pagess implements PagesBase {
    BASKET(1, "basket"),
    TIMESLOT(2, "timeslot"),
    CONFIRMATION(3, "confirmation"),
    AFTERSALE(4, "aftersale");

    final int order;
    final String url;

    private Pagess(int order, String url) {
        this.order = order;
        this.url = url;
    }
    public String getUrl() {
        return this.url;
    }

    public static Pagess find(int page, Supplier<? extends Pagess> byDef) {
        return Arrays.asList(Pagess.values()).stream()
                .filter(e -> e.order == page).findFirst().orElseGet(byDef);
    }

    private static final Map lookup =
            new HashMap();
    static {
        for(Pagess page : Pagess.values())
            lookup.put(page.getOrder(), page);
    }

    public int getOrder() { return order; }

    public static Pagess get(int order) {
        return (Pagess) lookup.get(order);
    }
}
