package com.kaitait.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
public class ReturnToTimeSlotValidator implements org.springframework.statemachine.guard.Guard<Pages, Events> {

    private static final Logger LOG = LoggerFactory.getLogger(ReturnToTimeSlotValidator.class);
    private TimeSlotValidator validator;
    @Autowired
    public ReturnToTimeSlotValidator(TimeSlotValidator validator){
        this.validator = validator;
    }

    @Override
    public boolean evaluate(final StateContext<Pages, Events> stateContext) {
        LOG.info("____ ReturnToTimeSlotValidator");
        // The valid state for this transition is if the timeslot validator is invalid
       return !validator.evaluate(stateContext);
    }

    public boolean isValid(boolean isValid) {
        return isValid;
    }
}
