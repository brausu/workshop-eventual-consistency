package org.ecd3.samples.shoppingcard.nonmonotonic.insertanddelete.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ShoppingCard extends RootEntity {

  private final UUID customerRef;

  /**
   * The line items of the shopping card.
   */
  private final Set<LineItem> lineItems;

  public ShoppingCard(UUID id, UUID customerRef) {
    super(id);
    this.lineItems = new HashSet<>();

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

  private void handleAddItemAction(AddItemAction a) {
    Product product = a.getProduct();
    LineItem lineItem = getLineItemByProduct(product);
    LineItem updatedLineItem = lineItem.increaseAmountBy(1);

    lineItems.remove(lineItem);
    lineItems.add(updatedLineItem);
  }

  private void handleDeleteItemAction(DeleteItemAction a) {
    Product product = a.getProduct();
    LineItem lineItem = getLineItemByProduct(product);
    LineItem updatedLineItem = lineItem.decreaseAmountBy(1);

    lineItems.remove(lineItem);
    if (updatedLineItem.getNumber() > 0) {
      lineItems.add(updatedLineItem);
    }
  }

  public Set<LineItem> getLineItems() {
    return this.lineItems;
  }

  public UUID getCustomerRef() {
    return customerRef;
  }

  private LineItem getLineItemByProduct(Product product) {
    LineItem lineItem = new LineItem(product, 1);

    return lineItems.stream().filter(lineItem::equals).findFirst().orElse(new LineItem(product, 0));
  }

}
