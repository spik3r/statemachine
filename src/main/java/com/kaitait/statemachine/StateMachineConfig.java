package com.kaitait.statemachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.ExtendedState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.access.StateMachineAccess;
import org.springframework.statemachine.access.StateMachineFunction;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.DefaultExtendedState;
import org.springframework.statemachine.support.DefaultStateMachineContext;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<Pages, Events> {
    private static final Logger LOG = LoggerFactory.getLogger(StateMachineConfig.class);

    @Autowired
    TimeSlotValidator timeSlotValidator;
    @Autowired
    BasketValidator basketValidator;

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
                    long basketId = long.class.cast(stateContext.getExtendedState().getVariables().getOrDefault("basketId", -1L));
                    LOG.info("Entering Basket State/Page. basketId: " + basketId);

                })
                .state(Pages.TIMESLOT)
                .state(Pages.CONFIRMATION)
                .end(Pages.AFTERSALE);

    }

    @Override
    public void configure(StateMachineTransitionConfigurer<Pages, Events> transitions)
            throws Exception {

        transitions
                .withExternal()
                .source(Pages.BASKET).target(Pages.TIMESLOT)
                .event(Events.BASKET_PAGE_SEEN)
                .event(Events.BASKET_CREATED)
                .guard(basketValidator)
                .and()

                .withExternal()
                .source(Pages.TIMESLOT).target(Pages.CONFIRMATION)
                .event(Events.TIMESLOT_PAGE_SEEN)
                .event(Events.TIMESLOT_SELECTED)
                .guard(timeSlotValidator)
                .and()

                .withExternal()
                .source(Pages.CONFIRMATION).target(Pages.AFTERSALE)
                .event(Events.CONFIRMATION_PAGE_SEEN)
                .event(Events.ORDER_CONFIRMED);
//                .and()

//                .withExternal()
//                .source(Pages.AFTERSALE).target(Pages.BASKET)
//                .action(myAction());

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
    public Action<Pages, Events> myAction() {
        return new Action<Pages, Events>() {

            @Override
            public void execute(StateContext<Pages, Events> context) {
                // do something
                LOG.info("____RESETTING STATEMACHINE");


                Map<Object, Object> variables = new HashMap<Object, Object>();
                variables.put("foo", 1);
//                variables.put(Pages.values(), Events.values());
                ExtendedState extendedState = new DefaultExtendedState(variables);
                DefaultStateMachineContext<Pages,Events> stateMachineContext = new DefaultStateMachineContext<Pages, Events>(Pages.BASKET, Events.BASKET_PAGE_SEEN, null, extendedState);
                LOG.info("____history: " + stateMachineContext.getHistoryStates());
                context.getStateMachine().getStateMachineAccessor().doWithAllRegions(new StateMachineFunction<StateMachineAccess<Pages, Events>>() {
                    @Override
                    public void apply(StateMachineAccess<Pages, Events> function) {
                        context.getStateMachine().stop();
                        function.resetStateMachine(stateMachineContext);
                        context.getStateMachine().getExtendedState().getVariables().clear();
                        context.getStateMachine().start();
                        context.getStateMachine().getInitialState();
                        LOG.info("____after apply state is: " + context.getStateMachine().getState());
                        LOG.info("____history: " + stateMachineContext.getHistoryStates());
                    }
                });
            }
        };
    }
}
