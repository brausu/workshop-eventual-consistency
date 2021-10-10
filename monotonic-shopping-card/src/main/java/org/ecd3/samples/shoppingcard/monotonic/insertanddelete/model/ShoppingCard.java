package org.ecd3.samples.shoppingcard.monotonic.insertanddelete.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ShoppingCard extends RootEntity {

  private final UUID customerRef;

  private final Map<Product, Integer> addedItems; // monotonic

  private final Map<Product, Integer> deletedItems; // monotonic

  private void handleAddItemAction(AddItemAction a) {
    addItemToSet(a.getProduct(), addedItems);
  }

  private void handleDeleteItemAction(DeleteItemAction a) {
    addItemToSet(a.getProduct(), deletedItems);
  }

  private void addItemToSet(Product product, Map<Product, Integer> itemSet) {
    if (itemSet.containsKey(product)) {
      Integer amount = itemSet.get(product);
      itemSet.put(product, ++amount);
    } else {
      itemSet.put(product, 1);
    }
  }

  public Set<LineItem> getLineItems() {
    Set<LineItem> result = new HashSet<>();
    for (Product product : this.addedItems.keySet()) {
      int addedAmount = this.addedItems.get(product);
      int deletedAmount = this.deletedItems.getOrDefault(product, 0);
      int finalAmount = addedAmount + (-deletedAmount);

      if (finalAmount > 0) {
        LineItem lineItem = new LineItem(product, finalAmount);
        result.add(lineItem);
      }
    }
    return result;
  }

  public UUID getCustomerRef() {
    return customerRef;
  }

  public ShoppingCard(UUID id, UUID customerRef) {
    super(id);
    this.addedItems = new HashMap<>();
    this.deletedItems = new HashMap<>();

    if (customerRef == null) {
      throw new IllegalArgumentException("Customer reference must not be null");
    }
    this.customerRef = customerRef;
  }

  public void handleShoppingCardAction(ShoppingCardAction a) {
    if (!this.customerRef.equals(a.getCustomerRef())) {
      throw new IllegalArgumentException("Action routed to wrong shopping card.");
    }

    if (a instanceof AddItemAction) {
      handleAddItemAction((AddItemAction) a);
    } else if (a instanceof DeleteItemAction) {
      handleDeleteItemAction((DeleteItemAction) a);
    }
  }

}
