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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class CheckoutController implements NextPage{

    @Autowired
    private StateMachine<Pages, Events> stateMachine;

    @Autowired
    TimeSlotValidator timeSlotValidator;

    private final ValidationService validationService = ValidationService.getValidator();

    private static final Logger LOG = LoggerFactory.getLogger(CheckoutController.class);

    @RequestMapping(path = "checkout/basket")
    public String basket(Model model) throws Exception {
        LOG.info("basket");
        LOG.info(String.valueOf("state: " + stateMachine.getState().getId().order));
        // Two ways to send events the quick way with sendEvent or sendEvent with a message incase you need headers etc
        Message<Events> eventsMessage = MessageBuilder
                .withPayload(Events.BASKET_PAGE_SEEN)
                .setHeader("header_foo", "bar")
                .build();
        stateMachine.sendEvent(eventsMessage);



        model.addAttribute("page", "basket");
        model.addAttribute("furthest", validationService.getFurthestPath(stateMachine.getState().getId()));
        return "basket";
    }

    @RequestMapping(path = "checkout/basket_submit", method = RequestMethod.GET)
    public ModelAndView basketSubmit(@RequestParam(name = "basketAmount", value = "basketAmount", required = false, defaultValue = "0") final int basketAmount) throws Exception {
        LOG.info("basketSubmit");

        LOG.info("basketAmount: " + basketAmount);
        stateMachine.getExtendedState().getVariables().put("basketAmount", basketAmount);

        stateMachine.sendEvent(Events.BASKET_CREATED);
//        stateMachine.setStateMachineError(new RuntimeException("fooo"));

        return stay();
    }



    @RequestMapping(path = "checkout/timeslot")
    public String timeslot(Model model, HttpServletResponse response) throws Exception {
        LOG.info("timeslot");
        LOG.info(String.valueOf("state: " + stateMachine.getState().getId().order));

        Message<Events> eventsMessage = MessageBuilder
                .withPayload(Events.TIMESLOT_PAGE_SEEN)
                .setHeader("timeslot_header", "fooooo")
                .build();
        stateMachine.sendEvent(eventsMessage);

        model.addAttribute("page", "timeslot");
        model.addAttribute("furthest", validationService.getFurthestPath(stateMachine.getState().getId()));
        return "timeslot";
    }


    @RequestMapping(path = "checkout/timeslot_submit", method = RequestMethod.GET)
    public ModelAndView timeslotSubmit(@RequestParam(value = "isValid", required = false, defaultValue = "false") final boolean timeSlotCheckbox) throws IOException {

        LOG.info("timeSlotId: " + timeSlotCheckbox);
        stateMachine.getExtendedState().getVariables().put("timeslotValid", timeSlotCheckbox);
        stateMachine.sendEvent(Events.TIMESLOT_SELECTED);

        return stay();
    }

    private ModelAndView goNext() {
        LOG.info("goNext");
        LOG.info(String.valueOf("state: " + stateMachine.getState().getId().order));
        final String redirectTarget = String.valueOf(validationService.getFurthestPath(stateMachine.getState().getId()));

//        final String redirectTarget = String.valueOf("aftersale");


//        stateMachine.sendEvent(event);


        return new ModelAndView(new RedirectView(redirectTarget));
    }

    private ModelAndView stay() {
        LOG.info("timeslotSubmit stay");

        final String redirectTarget = String.valueOf(stateMachine.getState().getId().getUrl());
        return new ModelAndView(new RedirectView(redirectTarget));
    }

    @RequestMapping(path = "checkout/confirmation")
    public String confirmation(Model model) {
        LOG.info("confirmation");
        stateMachine.sendEvent(Events.CONFIRMATION_PAGE_SEEN);
        model.addAttribute("page", "confirmation");
        model.addAttribute("furthest", validationService.getFurthestPath(stateMachine.getState().getId()));
        return "confirmation";
    }

    @RequestMapping(path = "checkout/confirmation_submit")
    public ModelAndView confirmationSubmit() {
        LOG.info("confirmationSubmit");
        stateMachine.sendEvent(Events.ORDER_CONFIRMED);

//        final String redirectTarget = String.valueOf(stateMachine.getState().getId().getUrl());
//        return new ModelAndView(new RedirectView(redirectTarget));
        return stay();
    }

    @RequestMapping(path = "checkout/aftersale")
    public String aftersale() {
        LOG.info("aftersale");
        LOG.info(String.valueOf("state: " + stateMachine.getState().getId().order));
        stateMachine.sendEvent(Events.AFTER_SALE_SEEN);
        return "aftersale";
    }

    @Override
    public void shouldGoNext() {
        goNext();
    }
}
