package org.ecd3.samples.shoppingcard.monotonic.insertonly.model;

import java.util.UUID;

public class AddItemAction extends ShoppingCardAction {

  public AddItemAction(UUID id, Product product, UUID customerRef) {
    super(id, product, customerRef);
  }
}
