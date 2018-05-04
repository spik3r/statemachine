package com.kaitait.statemachine;

import org.springframework.stereotype.Component;

@Component
public interface NextPage {
    void shouldGoNext();

    void invalidateBasket();
}
