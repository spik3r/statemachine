package com.kaitait.statemachine;

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
}
