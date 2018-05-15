package com.kaitait.statemachine;

import java.util.Objects;
import java.util.UUID;

public class CheckoutModel {
     private int basketAmount;
     private String addressId;
     private boolean timeslotSelected;
     private boolean paymentSelected;
     private UUID confirmationId;

    public CheckoutType getType() {
        return type;
    }

    private final CheckoutType type;

    public CheckoutModel(CheckoutType type) {
        this.type = type;
    }
     public void setBasketAmount(int basketAmount) {
          this.basketAmount = basketAmount;
     }

     public int getBasketAmount() {
          return basketAmount;
     }

     public void setAddressId(final String addressId) {
          this.addressId = addressId;
     }

     public String getAddressId() {
          return addressId;
     }

     public void setPaymentSelected(final boolean paymentSelected) {
          this.paymentSelected = paymentSelected;
     }

     public boolean getPaymentSelected() {
          return paymentSelected;
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
     public boolean equals(final Object o) {
          if (this == o)
               return true;
          if (o == null || getClass() != o.getClass())
               return false;

          final CheckoutModel that = (CheckoutModel) o;

          if (basketAmount != that.basketAmount)
               return false;
          if (timeslotSelected != that.timeslotSelected)
               return false;
          if (paymentSelected != that.paymentSelected)
               return false;
          if (addressId != null ? !addressId.equals(that.addressId) : that.addressId != null)
               return false;
          return confirmationId != null ? confirmationId.equals(that.confirmationId) : that.confirmationId == null;
     }

     @Override
     public int hashCode() {
          int result = basketAmount;
          result = 31 * result + (addressId != null ? addressId.hashCode() : 0);
          result = 31 * result + (timeslotSelected ? 1 : 0);
          result = 31 * result + (paymentSelected ? 1 : 0);
          result = 31 * result + (confirmationId != null ? confirmationId.hashCode() : 0);
          return result;
     }
}
