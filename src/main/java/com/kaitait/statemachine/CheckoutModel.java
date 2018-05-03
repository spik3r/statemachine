package com.kaitait.statemachine;

import org.immutables.value.Value;

@Value.Immutable
public interface CheckoutModel {
     String basketId();
     String timeslotId();
     String confirmationId();
}
