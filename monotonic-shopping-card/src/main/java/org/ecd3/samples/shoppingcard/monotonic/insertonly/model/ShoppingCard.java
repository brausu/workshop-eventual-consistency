package org.ecd3.samples.shoppingcard.monotonic.insertonly.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ShoppingCard extends RootEntity {

  /**
   * Reference to the customer aggregate.
   */
  private final UUID customerRef;

  /**
   * The line items of the shopping card.
   */
  private final Set<LineItem> lineItems;

  public ShoppingCard(UUID id, UUID customerRef) {
    super(id);
    this.lineItems = new HashSet<>();

    if(customerRef == null) throw  new IllegalArgumentException("Customer reference must not be null");
    this.customerRef = customerRef;
  }

  /**
   * Handles an add item action.
   */
  public void handleAddItemAction(AddItemAction a) {
    Product product = a.getProduct();
    LineItem lineItem = getLineItemByProduct(product);
    LineItem updatedLineItem = lineItem.increaseAmountBy(1);

    lineItems.remove(lineItem);
    lineItems.add(updatedLineItem);
  }

  public Set<LineItem> getLineItems() {
    return this.lineItems;
  }

  public UUID getCustomerRef() {
    return customerRef;
  }

  /**
   * Get the line item corresponding with a product
   * @param product the product
   * @return the existing line item or a new line item with zero amount.
   */
  private LineItem getLineItemByProduct(Product product) {
    LineItem lineItem = new LineItem(product, 1);

    return lineItems.stream().filter(lineItem::equals).findFirst()
        .orElse(new LineItem(product, 0));
  }
}
