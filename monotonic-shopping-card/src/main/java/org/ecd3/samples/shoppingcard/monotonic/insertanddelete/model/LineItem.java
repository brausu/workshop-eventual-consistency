package org.ecd3.samples.shoppingcard.monotonic.insertanddelete.model;

import java.util.Objects;
import org.javamoney.moneta.Money;


public class LineItem extends ValueObject {

  private final String productRef;

  private final String name;

  private final Money singleItemPrice;

  private final int number;

  public LineItem(String productRef, String name, Money price, int number) {
    this.productRef = productRef;
    this.name = name;
    this.singleItemPrice = price;
    this.number = number;
  }

  public LineItem(Product product, int number) {
    if(product == null) throw  new IllegalArgumentException("Product must not be null");
    this.productRef = product.getProductId();
    this.name = product.getName();
    this.singleItemPrice = product.getPrice();
    this.number = number;
  }

  public LineItem increaseAmountBy(int i) {
    int newAmount = this.number + i;
    return new LineItem(this.productRef, this.name, this.singleItemPrice, newAmount);
  }

  public String getProductRef() {
    return productRef;
  }

  public int getNumber() {
    return number;
  }

  public String getName() {
    return name;
  }

  public Money getSingleItemPrice() {
    return singleItemPrice;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof LineItem)) {
      return false;
    }
    LineItem lineItem = (LineItem) o;
    return Objects.equals(productRef, lineItem.productRef) && Objects.equals(name, lineItem.name)
        && Objects.equals(singleItemPrice, lineItem.singleItemPrice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productRef, name, singleItemPrice);
  }


}
