package km1.algafood;

import static km1.algafood.utils.RestaurantTestBuilder.aRestaurant;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import km1.algafood.domain.exceptions.DomainException;
import km1.algafood.domain.exceptions.RestaurantNotFountException;
import km1.algafood.domain.models.Restaurant;
import km1.algafood.domain.services.RestaurantRegisterService;

@SpringBootTest
@ActiveProfiles("test")
public class RestaurantRegisterIntTests {

  private static final String TEST_DATA = "/querys/test_data.sql";
  private static final String TRUNCATE_TABLES = "/querys/truncate_tables.sql";
  private static final long WITHOUT_DEPENDENTS_ID = 3l;
  private static final long INVALID_ID = 100l;
  private static final long VALID_ID = 1l;
  @Autowired private RestaurantRegisterService underTest;

  @BeforeEach
  void setup() {
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldRegisterRestaurant_whenTryRegisterValidRestaurant() {
    var restaurant = aRestaurant().build();
    Restaurant registered = underTest.register(restaurant);
    assertThat(registered.getId(), notNullValue());
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldReturnRestaurant_whenTryFetchValidRestaurant() {
    var restaurant = underTest.tryFetch(VALID_ID);
    assertThat(restaurant, notNullValue(Restaurant.class));
    assertThat(restaurant.getId(), is(VALID_ID));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldReturnAllRestaurants_whenTryFetchAllRestaurants() {
    var restaurants = underTest.fetchAll();
    assertThat(restaurants, notNullValue());
    assertThat(restaurants, hasSize(3));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldThrowRestaurantNotFound_whenTryFetchValidRestaurant() {
    assertThrows(RestaurantNotFountException.class, () -> underTest.tryFetch(INVALID_ID));
  }

  @Test @Sql({TRUNCATE_TABLES, TEST_DATA}) void shouldThrowDomainException_whenTryRegisterRestaurantWithNullName() {
    Restaurant restaurant = aRestaurant().withNullName().build();
    assertThrows(DomainException.class, () -> underTest.register(restaurant));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldThrowRestaurantHasDependents_whenTryRemoveRestaurantWhileHasDependents() {
    assertThrows(DomainException.class, () -> underTest.remove(1l));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldThrowRestaurantNotFound_whenTryRemoveUnregisteredRestaurant() {
    assertThrows(RestaurantNotFountException.class, () -> underTest.remove(INVALID_ID));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldRemoveRestaurant_whenTryRemoveValidRestaurant() {
    underTest.remove(WITHOUT_DEPENDENTS_ID);
    assertThrows(RestaurantNotFountException.class, () -> underTest.tryFetch(WITHOUT_DEPENDENTS_ID));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldThrowDomainException_whenTryUpdateRestaurantWithNullName() {
    var toUpdate = aRestaurant().build();
    assertThrows(DomainException.class, () -> underTest.update(VALID_ID,toUpdate));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldThrowRestaurantNotFoundException_whenTryUpdateUnregisteredRestaurant() {
    var toUpdate = aRestaurant().build();
    assertThrows(DomainException.class, () -> underTest.update(INVALID_ID,toUpdate));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldReturnUpdatedRestaurant_whenTryUpdateValidRestaurant() {
    var toUpdate = aRestaurant().build();
    var updated = underTest.update(VALID_ID, toUpdate);
    assertThat(updated.getName(),is(toUpdate.getName()));
    assertThat(updated.getId(),is(VALID_ID));
  }
}
