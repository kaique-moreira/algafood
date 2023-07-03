package km1.algafood.domain.models;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

@Getter
public enum OrderStatus {
  CREATED("Created"),
  CONFIRMED("Confirmed", CREATED),
  DELIVERED("Delivered", CONFIRMED),
  CANCELED("Canceled", CREATED);

  private String description;
  private List<OrderStatus> previus;

  private OrderStatus(String description, OrderStatus... previus) {
    this.description = description;
    this.previus = Arrays.asList(previus);
  }

  public boolean cannotChange(OrderStatus status){
    return !status.previus.contains(this);
  }

}
