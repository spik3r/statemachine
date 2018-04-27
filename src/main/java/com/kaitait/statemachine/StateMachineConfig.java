package com.kaitait.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<Pages, Events> {
    private static final Logger LOG = LoggerFactory.getLogger(StateMachineConfig.class);

    @Override
    public void configure(StateMachineConfigurationConfigurer<Pages, Events> config)
            throws Exception {

        config
                .withConfiguration()
                .autoStartup(true)
                .listener(listener());
    }

    @Override
    public void configure(StateMachineStateConfigurer<Pages, Events> states)
            throws Exception {

        states
                .withStates()
                .initial(Pages.BASKET)
                .stateEntry(Pages.BASKET, stateContext -> {
                    Long basketId = long.class.cast(stateContext.getExtendedState().getVariables().getOrDefault("basketId", -1L));
                    LOG.info("Entering Basket State/Page. basketId: " + basketId);

                })
                .state(Pages.ADDRESS)
                .state(Pages.TIMESLOT)
                .state(Pages.PAYMENT)
                .state(Pages.CONFIRMATION)
                .end(Pages.AFTERSALE);

//        states
//                .withStates()
//                .initial(Pages.BASKET)
//                .end(Pages.AFTERSALE)
//
//               .states(EnumSet.allOf(Pages.class));
/*  an option

        states
                .withStates()
                .initial(Pages.BASKET)
                .state(Pages.BASKET)
                .and()
                .withStates()
                    .parent(Pages.BASKET)
                    .initial(Pages.ADDRESS)
                    .state(Pages.ADDRESS)
                .and()
                .withStates()
                    .parent(Pages.ADDRESS)
                    .initial(Pages.TIMESLOT)
                    .state(Pages.TIMESLOT)
                .and()
                .withStates()
                    .parent(Pages.TIMESLOT)
                    .initial(Pages.PAYMENT)
                    .state(Pages.PAYMENT)
                .and()
                .withStates()
                    .parent(Pages.PAYMENT)
                    .initial(Pages.CONFIRMATION)
                    .state(Pages.CONFIRMATION)
                .and()
                .withStates()
                    .parent(Pages.CONFIRMATION)
                    .initial(Pages.AFTERSALE)
                    .state(Pages.AFTERSALE)

                .end(Pages.AFTERSALE);*/
//                .states(EnumSet.allOf(Pages.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<Pages, Events> transitions)
            throws Exception {

        transitions
                .withExternal()
                .source(Pages.BASKET).target(Pages.ADDRESS).event(Events.BASKET_CREATED)
                .and()
                .withExternal()
                .source(Pages.ADDRESS).target(Pages.TIMESLOT).event(Events.ADDRESS_SELECTED)
                .and()
                .withExternal()
                .source(Pages.TIMESLOT).target(Pages.PAYMENT).event(Events.TIMESLOT_SELECTED)
                .and()
                .withExternal()
                .source(Pages.PAYMENT).target(Pages.CONFIRMATION).event(Events.PAYMENT_METHOD_SELECTED)
                .and()
                .withExternal()
                .source(Pages.CONFIRMATION).target(Pages.AFTERSALE).event(Events.ORDER_CONFIRMED);

    }

    @Bean
    public StateMachineListener<Pages, Events> listener() {

        return new StateMachineListenerAdapter<Pages, Events>() {
            @Override
            public void stateChanged(State<Pages, Events> from, State<Pages, Events> to) {

                if (from == null) {
                    LOG.info("State machine initialised in state " + to.getId());
                } else {
                    LOG.info("State changed from " + from.getId() + " to " + to.getId());
                }
            }
        };
    }

    @Bean
    public Guard<Pages, Events> guard() {
        LOG.info("Guard");
        return context -> false;
    }
}
