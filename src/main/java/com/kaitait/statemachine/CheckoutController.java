package com.kaitait.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import java.io.IOException;

@Controller
public class CheckoutController implements NextPage{

    @Autowired
    private StateMachine<Pages, Events> stateMachine;

    @Autowired
    TimeSlotValidator timeSlotValidator;

    private final ValidationService validationService = ValidationService.getValidator();

    private static final Logger LOG = LoggerFactory.getLogger(CheckoutController.class);
    private CheckoutModel checkoutModel;

    @RequestMapping(path = "checkout")
    public String checkout(Model model) throws Exception {
        LOG.info("checkout");
        model.addAttribute("page", "checkout");
        return "checkout";
    }

    @RequestMapping(path = "checkout/start")
    public String checkout(Model model, @RequestParam(name = "type", value = "type", required = false) final String type) throws Exception {
        LOG.info("checkouttype in : " + type);
        LOG.info("_____ checkouttype enum: " + CheckoutType.getByType(type));
//        model.addAttribute("type", CheckoutType.getByType(type));
        model.addAttribute("type", type);

        checkoutModel = new CheckoutModel(CheckoutType.getByType(type));
        //Todo:
        return "basket";
    }

    @RequestMapping(path = "checkout/basket")
    public String basket(Model model, @RequestParam(name = "type", value = "type", required = false, defaultValue = "delivery") final String type) throws Exception {
        LOG.info("basket");
        LOG.info(String.valueOf("state: " + stateMachine.getState().getId().getDeliveryOrder()));
        LOG.info("_____ checkouttype enum: " + CheckoutType.getByType(type));
        Message<Events> eventsMessage = MessageBuilder
                .withPayload(Events.BASKET_PAGE_SEEN)
                .setHeader("type", type)
                .build();

        stateMachine.sendEvent(eventsMessage);

        model.addAttribute("page", "basket");
        model.addAttribute("furthest", validationService.getFurthestPath(stateMachine.getState().getId(), checkoutModel.getType()));
        model.addAttribute("basketAmount", checkoutModel.getBasketAmount());
        model.addAttribute("type", type);

        stateMachine.getExtendedState().getVariables().put("basketAmount", checkoutModel.getBasketAmount());
        LOG.info("____ BASKET CO MODEL: " + checkoutModel.getBasketAmount());
        return "basket";
    }

    @RequestMapping(path = "checkout/basket_submit", method = RequestMethod.GET)
    public ModelAndView basketSubmit(Model model, @RequestParam(name = "basketAmount", value = "basketAmount", required = false, defaultValue = "0") final int basketAmount) throws Exception {
        LOG.info("basketSubmit");
        model.addAttribute("type", checkoutModel.getType().name());

        LOG.info("basketAmount: " + basketAmount);
        model.addAttribute("basketAmount", basketAmount);
        checkoutModel.setBasketAmount(basketAmount);
        stateMachine.getExtendedState().getVariables().put("checkoutModel", checkoutModel);

        if (checkoutModel.getType() == CheckoutType.DELIVERY) {
            stateMachine.sendEvent(Events.BASKET_CREATED_DELIVERY);
        } else {
            stateMachine.sendEvent(Events.BASKET_CREATED);
        }

        return stay("basket");
    }

    @RequestMapping(path = "checkout/address")
    public ModelAndView address(Model model) throws Exception {
        LOG.info("address");
        LOG.info(String.valueOf("state: " + stateMachine.getState().getId().getDeliveryOrder()));
        model.addAttribute("type", checkoutModel.getType().name());
        Message<Events> eventsMessage = MessageBuilder
                .withPayload(Events.ADDRESS_PAGE_SEEN)
                .setHeader("header_foo", "bar")
                .build();
        stateMachine.sendEvent(eventsMessage);

        model.addAttribute("page", "address");
        model.addAttribute("furthest", validationService.getFurthestPath(stateMachine.getState().getId(), checkoutModel.getType()));
        model.addAttribute("addressId", checkoutModel.getAddressId());
        LOG.info("____ ADDRESS CO MODEL: " + checkoutModel.getAddressId());
       // stateMachine.getExtendedState().getVariables().put("addressId", checkoutModel.getAddressId());
        return stay("address");
//        return "address";
    }

    @RequestMapping(path = "checkout/address_submit", method = RequestMethod.GET)
    public ModelAndView addressSubmit(Model model, @RequestParam(name = "addressId", value = "addressId", required = false, defaultValue = "abc-123") final String addressId) throws Exception {
        LOG.info("addressSubmit");
        model.addAttribute("type", checkoutModel.getType().name());
        LOG.info("addressId: " + addressId);
        model.addAttribute("addressId", addressId);
        checkoutModel.setAddressId(addressId);
        stateMachine.getExtendedState().getVariables().put("checkoutModel", checkoutModel);
        stateMachine.sendEvent(Events.ADDRESS_SELECTED);

        return stay("address");
    }

    @RequestMapping(path = "checkout/timeslot")
    public ModelAndView timeslot(Model model) throws Exception {

        LOG.info("timeslot");
        LOG.info(String.valueOf("state: " + stateMachine.getState().getId().getDeliveryOrder()));
        model.addAttribute("type", checkoutModel.getType().name());
        Message<Events> eventsMessage = MessageBuilder
                .withPayload(Events.TIMESLOT_PAGE_SEEN)
                .setHeader("timeslot_header", "fooooo")
                .build();
        stateMachine.sendEvent(eventsMessage);

        model.addAttribute("page", "timeslot");
        model.addAttribute("furthest", validationService.getFurthestPath(stateMachine.getState().getId(), checkoutModel.getType()));
        model.addAttribute("timeslotSelected", checkoutModel.getTimeslotSelected());

        stateMachine.getExtendedState().getVariables().put("timeslotSelected", checkoutModel.getTimeslotSelected());
        LOG.info("____ TIMESLOT CO MODEL: " + checkoutModel.getTimeslotSelected());
        return stay("timeslot");
//        return "timeslot";
    }


    @RequestMapping(path = "checkout/timeslot_submit", method = RequestMethod.GET)
    public ModelAndView timeslotSubmit(Model model, @RequestParam(name = "timeslotSelected", value = "timeslotSelected", required = false, defaultValue = "false") final boolean timeslotSelected) throws IOException {
        model.addAttribute("type", checkoutModel.getType().name());
        LOG.info("###___ timeslotSelected in: " + timeslotSelected);
        model.addAttribute("timeslotSelected", timeslotSelected);
        checkoutModel.setTimeslotSelected(timeslotSelected);
        stateMachine.getExtendedState().getVariables().put("checkoutModel", checkoutModel);
        if (checkoutModel.getType() == CheckoutType.DELIVERY) {
            stateMachine.sendEvent(Events.TIMESLOT_SELECTED_DELIVERY);
        } else {
            stateMachine.sendEvent(Events.TIMESLOT_SELECTED);
        }

        return stay("timeslot");
    }


    @RequestMapping(path = "checkout/payment")
    public ModelAndView payment(Model model) throws Exception {
        LOG.info("payment");
        LOG.info(String.valueOf("state: " + stateMachine.getState().getId().getDeliveryOrder()));
        model.addAttribute("type", checkoutModel.getType().name());
        Message<Events> eventsMessage = MessageBuilder
                .withPayload(Events.PAYMENT_PAGE_SEEN)
                .setHeader("payment_header", "fooooo")
                .build();
        stateMachine.sendEvent(eventsMessage);

        model.addAttribute("page", "payment");
        model.addAttribute("furthest", validationService.getFurthestPath(stateMachine.getState().getId(), checkoutModel.getType()));
        model.addAttribute("paymentSelected", checkoutModel.getPaymentSelected());

        stateMachine.getExtendedState().getVariables().put("paymentSelected", checkoutModel.getPaymentSelected());
        LOG.info("____ PAYMENT CO MODEL: " + checkoutModel.getPaymentSelected());
//        return "payment";
        return stay("payment");
    }


    @RequestMapping(path = "checkout/payment_submit", method = RequestMethod.GET)
    public ModelAndView paymentubmit(Model model, @RequestParam(name = "paymentSelected", value = "paymentSelected", required = false, defaultValue = "false") final boolean paymentSelected) throws IOException {
        model.addAttribute("type", checkoutModel.getType().name());
        LOG.info("###___ timeslotSelected in: " + paymentSelected);
        model.addAttribute("timeslotSelected", paymentSelected);
        checkoutModel.setPaymentSelected(paymentSelected);
        stateMachine.getExtendedState().getVariables().put("checkoutModel", checkoutModel);

        stateMachine.sendEvent(Events.PAYMENT_SELECTED);

        return stay("payment");
    }

    @RequestMapping(path = "checkout/confirmation")
    public ModelAndView confirmation(Model model) {
        LOG.info("confirmation");
        stateMachine.sendEvent(Events.CONFIRMATION_PAGE_SEEN);
        model.addAttribute("type", checkoutModel.getType().name());


        model.addAttribute("page", "confirmation");
        model.addAttribute("furthest", validationService.getFurthestPath(stateMachine.getState().getId(), checkoutModel.getType()));
//        return "confirmation";
        return stay("confirmation");
    }

    @RequestMapping(path = "checkout/confirmation_submit")
    public ModelAndView confirmationSubmit(Model model) {
        LOG.info("confirmationSubmit");
        stateMachine.sendEvent(Events.ORDER_CONFIRMED);
        model.addAttribute("type", checkoutModel.getType().name());
        return stay("confirmation");
    }

    @RequestMapping(path = "checkout/aftersale")
    public String aftersale(Model model) {
        LOG.info("aftersale");
        model.addAttribute("type", checkoutModel.getType().name());
        LOG.info(String.valueOf("state: " + stateMachine.getState().getId().getDeliveryOrder()));
        stateMachine.sendEvent(Events.AFTER_SALE_SEEN);
        return "aftersale";
    }

    private void skipState(CheckoutModel checkoutModel) {

    }

    private ModelAndView goNext() {
        LOG.info("goNext");
        LOG.info(String.valueOf("state: " + stateMachine.getState().getId().getDeliveryOrder()));
        final String redirectTarget = String.valueOf(validationService.getFurthestPath(stateMachine.getState().getId(), checkoutModel.getType()));

        return new ModelAndView(new RedirectView(redirectTarget));
    }

    private ModelAndView stay(String requestUrl) {
        LOG.info("timeslotSubmit stay");

        final String redirectTarget = String.valueOf(stateMachine.getState().getId().getUrl());
        if (requestUrl.equalsIgnoreCase(redirectTarget)) {
            return new ModelAndView(redirectTarget);
        }
        return new ModelAndView(new RedirectView(redirectTarget));

//        return new ModelAndView(redirectTarget);
    }

    @Override
    public void shouldGoNext() {
        goNext();
    }

    @Override
    public void invalidateBasket() {
        LOG.info("___ invalidateBasket");
        stateMachine.sendEvent(Events.BASKET_CHANGED);
    }
    @Override
    public void invalidateTimeslot() {
        LOG.info("___ invalidateTimeslot");
        stateMachine.sendEvent(Events.TIMESLOT_CHANGED);
    }
}
