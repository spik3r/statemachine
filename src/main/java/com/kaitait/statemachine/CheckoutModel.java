package com.kaitait.statemachine;

import java.util.Objects;
import java.util.UUID;

public class CheckoutModel {
     private int basketAmount;
     private boolean timeslotSelected;
     private UUID confirmationId;

     public void setBasketAmount(int basketAmount) {
          this.basketAmount = basketAmount;
     }

     public int getBasketAmount() {
          return basketAmount;
     }

     public void setTimeslotSelected(boolean timeslotSelected) {
          this.timeslotSelected = timeslotSelected;
     }

     public boolean getTimeslotSelected() {
          return timeslotSelected;
     }

     public void setConfirmationId(UUID confirmationId) {
          this.confirmationId = confirmationId;
     }

     public UUID getConfirmationId() {
          return confirmationId;
     }

     @Override
     public boolean equals(Object o) {
          if (this == o) return true;
          if (o == null || getClass() != o.getClass()) return false;
          CheckoutModel that = (CheckoutModel) o;
          return Objects.equals(basketAmount, that.basketAmount) &&
                  Objects.equals(timeslotSelected, that.timeslotSelected) &&
                  Objects.equals(confirmationId, that.confirmationId);
     }

     @Override
     public int hashCode() {

          return Objects.hash(basketAmount, timeslotSelected, confirmationId);
     }
}
