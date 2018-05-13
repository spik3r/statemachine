package com.kaitait.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
public class BasketValidator implements org.springframework.statemachine.guard.Guard<Pages, Events> {

    private static final Logger LOG = LoggerFactory.getLogger(BasketValidator.class);
    public BasketValidator(){}

    @Override
    public boolean evaluate(final StateContext<Pages, Events> stateContext) {
        LOG.info(String.valueOf(stateContext.getExtendedState().getVariables().get("basketAmount")));
        LOG.info("____ CONFIG: " + stateContext.getExtendedState().getVariables());
//        return (Integer)stateContext.getExtendedState().getVariables().get("basketAmount") >= 40;
        final CheckoutModel checkoutModel = (CheckoutModel) stateContext.getExtendedState().getVariables().get("checkoutModel");
        return checkoutModel.getBasketAmount() >= 40;
    }

}
