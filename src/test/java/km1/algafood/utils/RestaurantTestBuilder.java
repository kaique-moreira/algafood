package km1.algafood.utils;

import km1.algafood.domain.models.Restaurant;

public class RestaurantTestBuilder {
  private final Long VALID_ID = 1l;
  private Restaurant restaurant =
      Restaurant.builder()
          .id(null)
          .name("")
          .addres(null)
          .cuisine(null)
          .products(null)
          .shippingFee(null)
          .updateDate(null)
          .registerDate(null)
          .paymentMethod(null)
          .build();

  public static RestaurantTestBuilder aRestaurant() {
    return new RestaurantTestBuilder();
  }

  public RestaurantTestBuilder withValidId() {
    this.restaurant.setId(VALID_ID);
    return this;
  }

  public RestaurantTestBuilder withNullName() {
    this.restaurant.setName(null);
    return this;
  }

  public Restaurant build() {
    return this.restaurant;
  }
}
