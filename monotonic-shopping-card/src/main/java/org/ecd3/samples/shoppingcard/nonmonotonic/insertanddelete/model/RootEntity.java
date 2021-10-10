package org.ecd3.samples.shoppingcard.nonmonotonic.insertanddelete.model;

import java.util.Objects;
import java.util.UUID;

public class RootEntity {

  protected UUID id;

  public RootEntity(UUID id) {
    if(id == null) throw new IllegalArgumentException("id must not be null.");
    this.id = id;
  }

  public UUID getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof RootEntity)) {
      return false;
    }
    RootEntity that = (RootEntity) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
