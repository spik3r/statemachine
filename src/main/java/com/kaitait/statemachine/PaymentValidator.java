package com.kaitait.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
public class PaymentValidator implements org.springframework.statemachine.guard.Guard<Pages, Events> {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentValidator.class);
    public PaymentValidator(){}

    @Override
    public boolean evaluate(final StateContext<Pages, Events> stateContext) {
        LOG.info(String.valueOf(stateContext.getExtendedState().getVariables().get("paymentSelected")));
        LOG.info("____ PaymentValidator: " + stateContext.getExtendedState().getVariables());
//        return (boolean) stateContext.getExtendedState().getVariables().get("timeslotValid");
        final CheckoutModel checkoutModel = (CheckoutModel) stateContext.getExtendedState().getVariables().get("checkoutModel");
        LOG.info("____+ checkoutModel: " + checkoutModel.getPaymentSelected());
        return checkoutModel.getTimeslotSelected();
    }

    public boolean isValid(boolean isValid) {
        return isValid;
    }
}
