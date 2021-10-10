package org.ecd3.samples.shoppingcard.monotonic.insertanddelete.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShoppingCardRepository {

  private static final ShoppingCardRepository instance = new ShoppingCardRepository();

  /**
   * Shopping Cards indexed by customer Ids.
   */
  private final Map<UUID, ShoppingCard> shoppingCards;

  private ShoppingCardRepository() {
    this.shoppingCards = new HashMap<>();
  }

  public synchronized ShoppingCard getShoppingCardByCustomerId(UUID customerRef) {
    ShoppingCard shoppingCard = this.shoppingCards.get(customerRef);
    if(shoppingCard == null) {
      shoppingCard = new ShoppingCard(UUID.randomUUID(), customerRef);
      this.shoppingCards.put(customerRef, shoppingCard);
    }

    return shoppingCard;
  }

  public synchronized void insert(ShoppingCard shoppingCard) {
    UUID customerRef = shoppingCard.getCustomerRef();
    this.shoppingCards.put(customerRef, shoppingCard);
  }

  public static ShoppingCardRepository getInstance() {
    return instance;
  }
}

