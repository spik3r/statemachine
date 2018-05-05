package com.kaitait.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;

@Component
public class ReturnToBasketValidator implements org.springframework.statemachine.guard.Guard<Pages, Events> {

    private static final Logger LOG = LoggerFactory.getLogger(ReturnToBasketValidator.class);
    private final BasketValidator validator;

    @Autowired
    public ReturnToBasketValidator(BasketValidator validator){
        this.validator = validator;
    }

    @Override
    public boolean evaluate(final StateContext<Pages, Events> stateContext) {

        LOG.info("____ ReturnToBasketValidator");
        // The valid state for this transition is if the basket validator is invalid
        return !validator.evaluate(stateContext);
    }

}
