package km1.algafood.domain.models;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import static km1.algafood.utils.RestaurantTestBuilder.aRestaurant;

public class RestaurantTests {

  @Test
  void shouldActive(){
    Restaurant restaurant = aRestaurant().disactived().build();
    restaurant.active();
    assertTrue(restaurant.getActive());
  } 

  @Test
  void shouldDisactive(){
    Restaurant restaurant = aRestaurant().actived().build();
    restaurant.disactive();
    assertFalse(restaurant.getActive());
  } 
}
