package com.kaitait.statemachine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public enum Pages implements PagesBase{
    BASKET(1, 1, "basket"),
    ADDRESS(2, 666, "address"),
    TIMESLOT(3, 2, "timeslot"),
    PAYMENT(4, 777, "payment"),
    CONFIRMATION(5, 3, "confirmation"),
    AFTERSALE(6, 4, "aftersale");

    final int deliveryOrder;
    final int pickupOrder;
    final String url;

    Pages(int deliveryOrder, int pickupOrder, String url) {
        this.deliveryOrder = deliveryOrder;
        this.pickupOrder = pickupOrder;
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    private static final Map lookup = new HashMap();
    private static final Map pickupLookup = new HashMap();
    static {
        for(Pages page : Pages.values())
            lookup.put(page.getDeliveryOrder(), page);

        for(Pages page : Pages.values())
            pickupLookup.put(page.getPickupOrder(), page);
    }

    public int getDeliveryOrder() { return deliveryOrder; }
    public int getPickupOrder() { return pickupOrder; }

    public static Pages get(int order) {
        return (Pages) lookup.get(order);
    }
    public static Pages getPickup(int order) {
        return (Pages) pickupLookup.get(order);
    }
}
