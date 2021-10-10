package org.ecd3.samples.shoppingcard.nonmonotonic.insertanddelete.model;

import java.util.UUID;

public class Customer extends RootEntity {

  private final String firstName;

  private String lastName;

  private Address shippingAddress;

  private Address billingAddress;

  public Customer(UUID id, String firstName, String lastName) {
    super(id);

    if(firstName == null) throw new IllegalArgumentException("First name must not be null.");
    this.firstName = firstName;

    if(lastName == null) throw new IllegalArgumentException("Last name must not be null.");
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public Address getShippingAddress() {
    return shippingAddress;
  }

  public Address getBillingAddress() {
    return billingAddress;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public void setShippingAddress(Address shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  public void setBillingAddress(Address billingAddress) {
    this.billingAddress = billingAddress;
  }

}
