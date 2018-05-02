package com.kaitait.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class CheckoutController {

    @Autowired
    private StateMachine<Pages, Events> stateMachine;

    private static final Logger LOG = LoggerFactory.getLogger(CheckoutController.class);

    @RequestMapping(path = "checkout/basket")
    public String basket(Model model) {
        LOG.info("basket");
        LOG.info(String.valueOf("state: " + stateMachine.getState().getId().order));
        // Two ways to send events the quick way with sendEvent or sendEvent with a message incase you need headers etc
        Message<Events> eventsMessage = MessageBuilder
                .withPayload(Events.BASKET_PAGE_SEEN)
                .setHeader("header_foo", "bar")
                .build();
        stateMachine.sendEvent(eventsMessage);
        model.addAttribute("page", "basket");
        return "basket";
    }

    @RequestMapping(path = "checkout/basket_submit")
    public String basketSubmit() {
        LOG.info("basketSubmit");
        LOG.info(String.valueOf("state: " + stateMachine.getState().getId().order));

        stateMachine.sendEvent(Events.BASKET_CREATED);
        return "basketSubmit";
    }



    @RequestMapping(path = "checkout/timeslot")
    public String timeslot(Model model, HttpServletResponse response) throws IOException {
        LOG.info("timeslot");
        LOG.info(String.valueOf("state: " + stateMachine.getState().getId().order));

        stateMachine.sendEvent(Events.TIMESLOT_PAGE_SEEN);
        model.addAttribute("page", "timeslot");
        return "timeslot";
    }


    @RequestMapping(path = "checkout/timeslot_submit")
    public String timeslotSubmit(HttpServletResponse response) throws IOException {
        LOG.info("timeslotSubmit");
        LOG.info(String.valueOf("state: " + stateMachine.getState().getId().order));
        if (!isOnCorrectPage(stateMachine.getState().getId(), Pages.TIMESLOT)) {
            return redirectIfOnWrongPage(response, stateMachine);
        }
        stateMachine.sendEvent(Events.TIMESLOT_SELECTED);
        return "timeslotSubmit";
    }

    @RequestMapping(path = "checkout/confirmation")
    public String confirmation(Model model) {
        LOG.info("confirmation");
        stateMachine.sendEvent(Events.CONFIRMATION_PAGE_SEEN);
        model.addAttribute("page", "confirmation");
        return "confirmation";
    }

    @RequestMapping(path = "checkout/confirmation_submit")
    public String confirmationSubmit() {
        LOG.info("confirmationSubmit");
        stateMachine.sendEvent(Events.ORDER_CONFIRMED);
        return "confirmationSubmit";
    }

    @RequestMapping(path = "checkout/aftersale")
    public String aftersale() {
        LOG.info("aftersale");
        LOG.info(String.valueOf("state: " + stateMachine.getState().getId().order));
        stateMachine.sendEvent(Events.AFTER_SALE_SEEN);
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

    private static String redirectIfOnWrongPage(HttpServletResponse response, StateMachine stateMachine) throws IOException {
        LOG.info("redirect");
        LOG.info("redirectUrl " + stateMachine.getState().toString());
        final String redirectUrl = stateMachine.getState().getId().toString().toLowerCase();
        response.sendRedirect(redirectUrl);
        return redirectUrl;
    }
}
