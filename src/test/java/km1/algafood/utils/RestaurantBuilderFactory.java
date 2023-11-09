package km1.algafood.utils;

import km1.algafood.domain.models.Restaurant;
import km1.algafood.domain.models.Restaurant.RestaurantBuilder;

public class RestaurantBuilderFactory {
  public static RestaurantBuilder validRestaurant() {
    return Restaurant.builder()
        .id(null)
        .name("")
        .addres(null)
        .cuisine(null)
        .products(null)
        .shippingFee(null)
        .updateDate(null)
        .registerDate(null)
        .paymentMethod(null);
  }

  public static RestaurantBuilder registeredRestaurant() {
    return Restaurant.builder()
        .id(1l)
        .name("")
        .addres(null)
        .cuisine(null)
        .products(null)
        .shippingFee(null)
        .updateDate(null)
        .registerDate(null)
        .paymentMethod(null);
  }
}
