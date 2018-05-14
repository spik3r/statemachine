package com.kaitait.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
public class TimeSlotValidator implements org.springframework.statemachine.guard.Guard<Pages, Events> {

    private static final Logger LOG = LoggerFactory.getLogger(TimeSlotValidator.class);
    public TimeSlotValidator(){}

    @Override
    public boolean evaluate(final StateContext<Pages, Events> stateContext) {
        LOG.info(String.valueOf(stateContext.getExtendedState().getVariables().get("timeslotValid")));
        LOG.info("____ TimeSlotValidator: " + stateContext.getExtendedState().getVariables());
//        return (boolean) stateContext.getExtendedState().getVariables().get("timeslotValid");
        final CheckoutModel checkoutModel = (CheckoutModel) stateContext.getExtendedState().getVariables().get("checkoutModel");
        LOG.info("____+ checkoutModel: " + checkoutModel.getTimeslotSelected());
        return checkoutModel.getTimeslotSelected();
    }

    public boolean isValid(boolean isValid) {
        return isValid;
    }
}
