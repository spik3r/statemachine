package com.kaitait.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
public class ReturnToBasketValidator implements org.springframework.statemachine.guard.Guard<Pages, Events> {

    private static final Logger LOG = LoggerFactory.getLogger(ReturnToBasketValidator.class);
    public ReturnToBasketValidator(){}

    @Override
    public boolean evaluate(final StateContext<Pages, Events> stateContext) {

        return true;
    }

}
