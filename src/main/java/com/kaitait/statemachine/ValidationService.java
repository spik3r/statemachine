package com.kaitait.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateMachine;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ValidationService {

    private static final Logger LOG = LoggerFactory.getLogger(ValidationService.class);

    private static StateMachine stateMachine;
    private ValidationService(StateMachine stateMachine){
        this.stateMachine = stateMachine;
    }
    private static ValidationService validationService;

    public static ValidationService getValidator() {
        if (validationService != null) {
            return validationService;
        }
        return new ValidationService(stateMachine);
    }

//    public static String getFurthestPath(Pages current) {
//        if (current.order == Pages.values().length) {
//            return Pages.BASKET.url;
//        }
//        if (current.order < Pages.values().length) {
//            return Pages.get(current.order +1).url;
//        }
//        return current.url;
//    }

    public static String getFurthestPath(Pages current, CheckoutType type) {

        if (type == CheckoutType.PICKUP) {
            String path = getFurthestPickupPath(current);
            LOG.info("getFurthestPickupPath: " + path);
            return path;
        }
        String path = getFurthestDeliveryPath(current);
        LOG.info("getFurthestDeliveryPath: " + path);
        return path;
    }

    private static String getFurthestDeliveryPath(Pages current) {
        if (current.deliveryOrder == Pages.values().length) {
            return Pages.BASKET.url;
        }
        if (current.deliveryOrder < Pages.values().length) {
            return Pages.get(current.deliveryOrder +1).url;
        }
        return current.url;
    }

    private static String getFurthestPickupPath(Pages current) {
        if (current.pickupOrder == Pages.values().length) {
            return Pages.BASKET.url;
        }
        if (current.pickupOrder < Pages.values().length) {
            return Pages.getPickup(current.pickupOrder +1).url;
        }
        return current.url;
    }
    
}
