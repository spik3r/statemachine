package com.kaitait.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateMachine;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ValidationService {

    private static final Logger LOG = LoggerFactory.getLogger(ValidationService.class);

    private ValidationService(){}
    private static final ValidationService validationService = new ValidationService();

    public static ValidationService getValidator() {
        return validationService;
    }


    static boolean isOnCorrectPage(Pages current, Pages previous) {
        LOG.info("oder: " + (previous.order - 1 < current.order));
        LOG.info("oder: " + (previous.order));
        LOG.info("oder: " + (current.order));
        if (current.order - 1 < previous.order) {
            return false;
        }
        return true;
    }

    static String getRedirectURL(HttpServletResponse response, StateMachine stateMachine) throws IOException {
        LOG.info("redirect");
        LOG.info("redirectUrl " + stateMachine.getState().toString());
        final String redirectUrl = stateMachine.getState().getId().toString().toLowerCase();
        response.sendRedirect(redirectUrl);
        return redirectUrl;
    }

    static String getFurthestPath(Pages current) {
        if (current.order <= Pages.values().length) {
            return Pages.get(current.order +1).url;
        }
        return current.url;
    }
}
