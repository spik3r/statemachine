package com.kaitait.statemachine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public enum Pages {
    BASKET(1, "basket"),
    TIMESLOT(2, "timeslot"),
    CONFIRMATION(3, "confirmation"),
    AFTERSALE(4, "aftersale");

    final int order;
    final String url;

    private Pages(int order, String url) {
        this.order = order;
        this.url = url;
    }
    public String getUrl() {
        return this.url;
    }

    public static Pages find(int page, Supplier<? extends Pages> byDef) {
        return Arrays.asList(Pages.values()).stream()
                .filter(e -> e.order == page).findFirst().orElseGet(byDef);
    }

    private static final Map lookup =
            new HashMap();
    static {
        for(Pages page : Pages.values())
            lookup.put(page.getOrder(), page);
    }

    public int getOrder() { return order; }

    public static Pages get(int order) {
        return (Pages) lookup.get(order);
    }
}
