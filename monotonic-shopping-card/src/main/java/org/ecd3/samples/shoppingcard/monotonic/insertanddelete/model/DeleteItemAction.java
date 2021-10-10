package org.ecd3.samples.shoppingcard.monotonic.insertanddelete.model;

import java.util.UUID;

public class DeleteItemAction extends ShoppingCardAction {

  public DeleteItemAction(UUID id, Product product, UUID customerRef) {
    super(id, product, customerRef);
  }
}
