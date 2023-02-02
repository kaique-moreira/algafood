package km1.algafood.utils;

import static km1.algafood.utils.AddresTestBuilder.aAddres;
import static km1.algafood.utils.CuisineTestBuilder.aCuisine;
import static km1.algafood.utils.ProductTestBuilder.aProduct;

import java.math.BigDecimal;
import java.util.Collections;

import km1.algafood.api.models.AddresInput;
import km1.algafood.api.models.CityIdInput;
import km1.algafood.api.models.CuisineIdInput;
import km1.algafood.api.models.CuisineModel;
import km1.algafood.api.models.RestaurantInput;
import km1.algafood.api.models.RestaurantModel;
import km1.algafood.domain.models.Addres;
import km1.algafood.domain.models.Cuisine;
import km1.algafood.domain.models.Restaurant;

public class RestaurantTestBuilder {
  private final Long VALID_ID = 1l;
  private Restaurant restaurant =
      Restaurant.builder()
          .id(null)
          .name("test")
          .addres(aAddres().build())
          .cuisine(aCuisine().withValidId().build())
          .products(Collections.singletonList(aProduct().build()))
          .shippingFee(BigDecimal.valueOf(1l))
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

  public RestaurantTestBuilder withBlankName() {
    this.restaurant.setName("");
    return this;
  }

  public RestaurantTestBuilder withNullShippingFee() {
    this.restaurant.setShippingFee(null);
    return this;
  }

  public RestaurantTestBuilder withNegativeShippingFee() {
    this.restaurant.setShippingFee(BigDecimal.valueOf(-1l));
    return this;
  }

  public RestaurantTestBuilder withCuisine(Cuisine cuisine) {
    this.restaurant.setCuisine(cuisine);
    return this;
  }

  public RestaurantTestBuilder withAddres(Addres addres){
    this.restaurant.setAddres(addres);
    return this;
  }

  public RestaurantTestBuilder disactived() {
    this.restaurant.setActive(false);
    return this;
  }

  public RestaurantTestBuilder actived() {
    this.restaurant.setActive(true);
    return this;
  }

  public Restaurant build() {
    return this.restaurant;
  }

  public RestaurantModel buildModel() {
    return RestaurantModel.builder()
        .name(this.restaurant.getName())
        .id(this.restaurant.getId())
        .cuisine(
            CuisineModel.builder()
                .name(this.restaurant.getCuisine().getName())
                .id(this.restaurant.getId())
                .build())
        .shippingFee(BigDecimal.valueOf(1l))
        .build();
  }

  public RestaurantInput buildInput() {
    return RestaurantInput.builder()
        .name(this.restaurant.getName())
        .cuisineIdInput(CuisineIdInput.builder().id(this.restaurant.getCuisine().getId()).build())
        .shippingFee(this.restaurant.getShippingFee())
        .addresiInput(
            AddresInput.builder()
                .number(this.restaurant.getAddres().getNumber())
                .street(this.restaurant.getAddres().getStreet())
                .district(this.restaurant.getAddres().getDistrict())
                .postalCode(this.restaurant.getAddres().getPostalCode())
                .complement(this.restaurant.getAddres().getComplement())
                .city(CityIdInput.builder().id(this.restaurant.getAddres().getCity().getId()).build())
                .build())
        .build();
  }
}
