package com.kaitait.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
public class AddressValidator implements org.springframework.statemachine.guard.Guard<Pages, Events> {

    private static final Logger LOG = LoggerFactory.getLogger(AddressValidator.class);
    public AddressValidator(){}

    @Override
    public boolean evaluate(final StateContext<Pages, Events> stateContext) {
        LOG.info(String.valueOf(stateContext.getExtendedState().getVariables().get("addressId")));
        LOG.info("____ AddressValidator: " + stateContext.getExtendedState().getVariables());
        final CheckoutModel checkoutModel = (CheckoutModel) stateContext.getExtendedState().getVariables().get("checkoutModel");
        return checkoutModel.getAddressId().length() >= 6;
    }

}
