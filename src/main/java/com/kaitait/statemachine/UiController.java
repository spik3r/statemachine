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

/**
 * Created by kai on 8/3/18.
 */
@Controller
public class UiController {

    private static final Logger LOG = LoggerFactory.getLogger(UiController.class);

    public UiController() {}

    @RequestMapping(value = "/ui", method = RequestMethod.GET)
    public String hello(Model model) {

        model.addAttribute("page", "basket");


        return "index";
    }
}
