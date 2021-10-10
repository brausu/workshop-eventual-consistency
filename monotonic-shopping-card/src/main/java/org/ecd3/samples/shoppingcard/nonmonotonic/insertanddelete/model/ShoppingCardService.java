package org.ecd3.samples.shoppingcard.nonmonotonic.insertanddelete.model;

import java.util.UUID;

public class ShoppingCardService {

  private static final ShoppingCardService instance = new ShoppingCardService();

  public void handleShoppingCardAction(ShoppingCardAction action) {
    ShoppingCard shoppingCard = ShoppingCardRepository.getInstance().getShoppingCardByCustomerId(action.getCustomerRef());
    if(shoppingCard == null) {
      shoppingCard = new ShoppingCard(UUID.randomUUID(), action.getCustomerRef());
      ShoppingCardRepository.getInstance().insert(shoppingCard);
    }

    shoppingCard.handleShoppingCardAction(action);;
  }

  public static ShoppingCardService getInstance() {
    return instance;
  }

}
