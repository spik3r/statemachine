package com.kaitait.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class CheckoutController {

    @Autowired
    private StateMachine<Pages, Events> stateMachine;

    private static final Logger LOG = LoggerFactory.getLogger(CheckoutController.class);

    @RequestMapping(path = "checkout/basket")
    public String basket() {
        LOG.info("basket");
        LOG.info(String.valueOf("state: " + stateMachine.getState().getId().order));
        // Two ways to send events the quick way with sendEvent or sendEvent with a message incase you need headers etc
//        stateMachine.sendEvent(Events.BASKET_CREATED);
        Message<Events> eventsMessage = MessageBuilder
                .withPayload(Events.INIT)
                .setHeader("header_foo", "bar")
                .build();
        stateMachine.sendEvent(eventsMessage);
        return "basket";
    }

    @RequestMapping(path = "checkout/address")
    public String address(HttpServletResponse response) throws IOException {
        LOG.info("address");
//        response.sendRedirect("basket");
//        LOG.info(String.valueOf(Pages.get(1)));
//        LOG.info(String.valueOf(Pages.get(2)));
        LOG.info(String.valueOf("state: " + stateMachine.getState().getId().order));
        if (!isOnCorrectPage(stateMachine.getState().getId(), Pages.ADDRESS)) {
            return redirectIfOnWrongPage(stateMachine.getState().getId(), Pages.ADDRESS, response);
        }
        stateMachine.sendEvent(Events.BASKET_CREATED);
        return "address";
    }

    @RequestMapping(path = "checkout/timeslot")
    public String timeslot(HttpServletResponse response) throws IOException {
        LOG.info("timeslot");
        LOG.info(String.valueOf("state: " + stateMachine.getState().getId().order));
        if (!isOnCorrectPage(stateMachine.getState().getId(), Pages.TIMESLOT)) {
            return redirectIfOnWrongPage(stateMachine.getState().getId(), Pages.TIMESLOT, response);
        }
        redirectIfOnWrongPage(stateMachine.getState().getId(), Pages.TIMESLOT, response);
        stateMachine.sendEvent(Events.ADDRESS_SELECTED);
        return "timeslot";
    }

    @RequestMapping(path = "checkout/payment")
    public String payment() {
        LOG.info("payment");
        LOG.info(String.valueOf("state: " + stateMachine.getState().getId().order));
        stateMachine.sendEvent(Events.TIMESLOT_SELECTED);
        return "payment";
    }

    @RequestMapping(path = "checkout/confirmation")
    public String confirmation() {
        LOG.info("confirmation");
        stateMachine.sendEvent(Events.PAYMENT_METHOD_SELECTED);
        return "confirmation";
    }

    @RequestMapping(path = "checkout/aftersale")
    public String aftersale() {
        LOG.info("aftersale");
        LOG.info(String.valueOf("state: " + stateMachine.getState().getId().order));
        stateMachine.sendEvent(Events.ORDER_CONFIRMED);
        return "aftersale";
    }

    private static boolean isOnCorrectPage(Pages current, Pages previous) {
        LOG.info("oder: " + (previous.order - 1 < current.order));
        LOG.info("oder: " + (previous.order));
        LOG.info("oder: " + (current.order));
        if (current.order - 1 < previous.order) {
            return false;
        }
        return true;
    }

    private static String redirectIfOnWrongPage(Pages current, Pages previous, HttpServletResponse response) throws IOException {
        if (!isOnCorrectPage(current, previous)) {
            LOG.info("redirect");
            final String redirectUrl = previous.toString().toLowerCase();
            response.sendRedirect(redirectUrl);
            return redirectUrl;
        }
        return "";
    }
}
