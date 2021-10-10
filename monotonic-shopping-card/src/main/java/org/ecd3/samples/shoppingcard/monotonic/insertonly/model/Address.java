package org.ecd3.samples.shoppingcard.monotonic.insertonly.model;

import java.util.Objects;

public class Address extends ValueObject {

  private final String firstName;

  private final String lastName;

  private final String streetName;

  private final String streetNumber;

  private final String city;

  private final String postalCode;

  private final String country;

  public Address(String firstName, String lastName, String streetName, String streetNumber, String city,
      String postalCode, String country) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.streetName = streetName;
    this.streetNumber = streetNumber;
    this.city = city;
    this.postalCode = postalCode;
    this.country = country;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getStreetName() {
    return streetName;
  }

  public String getStreetNumber() {
    return streetNumber;
  }

  public String getCity() {
    return city;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public String getCountry() {
    return country;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Address)) {
      return false;
    }
    Address address = (Address) o;
    return Objects.equals(firstName, address.firstName) && Objects.equals(lastName, address.lastName)
        && Objects.equals(streetName, address.streetName) && Objects
        .equals(streetNumber, address.streetNumber) && Objects.equals(city, address.city) && Objects
        .equals(postalCode, address.postalCode) && Objects.equals(country, address.country);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName, streetName, streetNumber, city, postalCode, country);
  }
}
