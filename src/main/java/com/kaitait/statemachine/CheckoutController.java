package com.kaitait.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckoutController {

    @Autowired
    private StateMachine<Pages, Events> stateMachine;

    private static final Logger LOG = LoggerFactory.getLogger(CheckoutController.class);

    @RequestMapping(path = "checkout/basket")
    public void basket() {
        LOG.info("basket");
        // Two ways to send events the quick way with sendEvent or sendEvent with a message incase you need headers etc
//        stateMachine.sendEvent(Events.BASKET_CREATED);
        Message<Events> eventsMessage = MessageBuilder
                .withPayload(Events.INIT)
                .setHeader("header_foo", "bar")
                .build();
        stateMachine.sendEvent(eventsMessage);
    }

    @RequestMapping(path = "checkout/address")
    public void address() {
        LOG.info("address");
        stateMachine.sendEvent(Events.BASKET_CREATED);
    }

    @RequestMapping(path = "checkout/timeslot")
    public void timeslot() {
        LOG.info("timeslot");
        stateMachine.sendEvent(Events.ADDRESS_SELECTED);
    }

    @RequestMapping(path = "checkout/payment")
    public void payment() {
        LOG.info("payment");
        stateMachine.sendEvent(Events.TIMESLOT_SELECTED);
    }

    @RequestMapping(path = "checkout/confirmation")
    public void confirmation() {
        LOG.info("confirmation");
        stateMachine.sendEvent(Events.PAYMENT_METHOD_SELECTED);
    }

    @RequestMapping(path = "checkout/aftersale")
    public void aftersale() {
        LOG.info("aftersale");
        stateMachine.sendEvent(Events.ORDER_CONFIRMED);
    }
}
