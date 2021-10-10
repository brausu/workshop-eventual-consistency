package org.ecd3.samples.shoppingcard.monotonic.insertonly.model;

import java.util.UUID;

public class ShoppingCardService {

  private final static ShoppingCardService instance = new ShoppingCardService();

  public void handleShoppingCardAction(ShoppingCardAction action) {
    ShoppingCard shoppingCard = ShoppingCardRepository.getInstance().getShoppingCardByCustomerId(action.getCustomerRef());
    if(shoppingCard == null) {
      shoppingCard = new ShoppingCard(UUID.randomUUID(), action.getCustomerRef());
      ShoppingCardRepository.getInstance().insert(shoppingCard);
    }

    if(action instanceof AddItemAction) {
      shoppingCard.handleAddItemAction((AddItemAction) action);
    }
  }

  public static ShoppingCardService getInstance() {
    return instance;
  }

}
