package org.ecd3.samples.shoppingcard.monotonic.insertonly.model;

import java.util.UUID;

public class ShoppingCardAction extends RootEntity {

  protected Product product;

  protected UUID customerRef;


  public ShoppingCardAction(UUID id,  Product product, UUID customerRef) {
    super(id);

    if(product == null) throw new IllegalArgumentException("Product must not be null.");
    this.product = product;

    if(customerRef == null) throw new IllegalArgumentException("Customer reference must not be null");
    this.customerRef = customerRef;
  }

  public Product getProduct() {
    return product;
  }

  public UUID getCustomerRef() {
    return customerRef;
  }
}
