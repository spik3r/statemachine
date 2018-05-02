package com.kaitait.statemachine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public enum Pages {
    BASKET(1, "checkout/basket"),
    TIMESLOT(2, "checkout/timeslot"),
    CONFIRMATION(3, "checkout/confirmation"),
    AFTERSALE(4, "checkout/aftersale");

    final int order;
    final String url;

    private Pages(int order, String url) {
        this.order = order;
        this.url = url;
    }

    public static Pages find(int page, Supplier<? extends Pages> byDef) {
        return Arrays.asList(Pages.values()).stream()
                .filter(e -> e.order == page).findFirst().orElseGet(byDef);
    }

    private static final Map lookup =
            new HashMap();
    static {
        //Create reverse lookup hash map
        for(Pages page : Pages.values())
            lookup.put(page.getOrder(), page);
    }

    public int getOrder() { return order; }

    public static Pages get(int order) {
        //the reverse lookup by simply getting
        //the value from the lookup HsahMap.
        return (Pages) lookup.get(order);
    }
}
