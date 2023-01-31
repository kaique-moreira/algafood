package km1.algafood.utils;

import java.math.BigDecimal;
import km1.algafood.api.models.CuisineDto;
import km1.algafood.api.models.CuisineIdInput;
import km1.algafood.api.models.RestaurantDto;
import km1.algafood.api.models.RestaurantInput;
import km1.algafood.domain.models.Cuisine;
import km1.algafood.domain.models.Restaurant;

public class RestaurantTestBuilder {
  private final Long VALID_ID = 1l;
  private Restaurant restaurant =
      Restaurant.builder()
          .id(null)
          .name("")
          .addres(null)
          .cuisine(Cuisine.builder().name("Francesa").build())
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
  
  public RestaurantTestBuilder disactived(){
    this.restaurant.setActive(false);
    return this;
  }

  public RestaurantTestBuilder actived(){
    this.restaurant.setActive(true);
    return this;
  }
  public Restaurant build() {
    return this.restaurant;
  }

  public RestaurantDto buildDto() {
    return RestaurantDto.builder()
        .name(this.restaurant.getName())
        .id(this.restaurant.getId())
        .cuisine(
            CuisineDto.builder()
                .name(this.restaurant.getCuisine().getName())
                .id(this.restaurant.getId())
                .build())
        .shippingFee(BigDecimal.valueOf(1l))
        .build();
  }

  public RestaurantInput buildInput() {
    return RestaurantInput.builder()
        .name(this.restaurant.getName())
        .cuisineIdInput(CuisineIdInput.builder().id(this.restaurant.getId()).build())
        .shippingFee(BigDecimal.valueOf(1l))
        .build();
  }
}
