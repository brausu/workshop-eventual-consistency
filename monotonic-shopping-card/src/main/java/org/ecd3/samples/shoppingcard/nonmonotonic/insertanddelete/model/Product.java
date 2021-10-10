package org.ecd3.samples.shoppingcard.nonmonotonic.insertanddelete.model;

import java.util.Objects;
import org.javamoney.moneta.Money;

public class Product extends ValueObject {

  private final String productId;

  private final String name;

  private final Money price;


  public Product(String productId, String name, Money price) {
    this.productId = productId;
    this.name = name;
    this.price = price;
  }

  public String getProductId() {
    return productId;
  }

  public String getName() {
    return name;
  }

  public Money getPrice() {
    return price;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Product)) {
      return false;
    }
    Product product = (Product) o;
    return Objects.equals(productId, product.productId) && Objects.equals(name, product.name)
        && Objects.equals(price, product.price);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productId, name, price);
  }
}
