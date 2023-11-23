package km1.algafood.domain.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import km1.algafood.domain.exceptions.DomainException;

public class ProductPictureTests {
  @Test
  void testProductId() {
    var underTest = ProductPicture.builder().product(Product.builder().id(1l).build()).build();
    assertEquals(1l, underTest.productId());
  }

  @Test
  void testProductId1() {
    var underTest = ProductPicture.builder().build();
    assertThrows(DomainException.class, () -> underTest.productId());
  }

  @Test
  void testRestaurantId() {
    var underTest = ProductPicture.builder()
        .product(Product.builder().restaurant(Restaurant.builder().id(1l).build()).build()).build();
    assertEquals(1l, underTest.restaurantId());
  }

  @Test
  void testRestaurantId1() {
    var underTest = ProductPicture.builder().product(Product.builder().build()).build();
    assertThrows(DomainException.class, () -> underTest.restaurantId());
  }

  @Test
  void testRestaurantId2() {
    var underTest = ProductPicture.builder().build();
    assertThrows(DomainException.class, () -> underTest.restaurantId());
  }
}
