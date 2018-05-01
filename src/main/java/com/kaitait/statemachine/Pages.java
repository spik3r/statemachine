package com.kaitait.statemachine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public enum Pages {
    BASKET(1),
    ADDRESS(2),
    TIMESLOT(3),
    PAYMENT(4),
    CONFIRMATION(5),
    AFTERSALE(6);

    final int order;

    private Pages(int order) {
        this.order = order;
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
