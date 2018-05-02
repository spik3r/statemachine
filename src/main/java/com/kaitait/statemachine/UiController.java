package com.kaitait.statemachine;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by kai on 8/3/18.
 */
@Controller
public class UiController {


    public UiController() {}

    @RequestMapping(value = "/ui", method = RequestMethod.GET)
    public String hello(Model model) {

        model.addAttribute("page", "basket");

        return "basket";
    }
}
