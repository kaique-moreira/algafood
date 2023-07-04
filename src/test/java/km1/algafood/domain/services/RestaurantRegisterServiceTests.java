package km1.algafood.domain.services;

import static km1.algafood.matchers.RestaurantMatcher.isRestaurantEqualTo;
import static km1.algafood.utils.RestaurantTestBuilder.*;
import static km1.algafood.utils.RestaurantTestBuilder.aRestaurant;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import km1.algafood.domain.exceptions.RestaurantHasDependents;
import km1.algafood.domain.exceptions.RestaurantNotFountException;
import km1.algafood.domain.models.Restaurant;
import km1.algafood.domain.repositories.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

@ExtendWith(MockitoExtension.class)
public class RestaurantRegisterServiceTests {

  private static final long VALID_ID = 1l;

  @Mock RestaurantRepository repository;
  @Mock CuisineRegisterService cuisineRegisterService;
  @Mock CityRegisterService cityRegisterService;

  @InjectMocks RestaurantRegisterService service;

  @Test
  void shouldThrowRestaurantNotFoundException_whenTryFetchWithUnregisteredRestaurantID() {
    when(repository.findById(VALID_ID)).thenReturn(Optional.empty());
    when(repository.findById(VALID_ID)).thenReturn(Optional.empty());

    assertThrows(RestaurantNotFountException.class, () -> service.tryFetch(VALID_ID));
  }

  @Test
  void shouldThrowRestaurantNotFoundException_whenTryRemoveWithUnregisteredRestaurantID() {
    doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(VALID_ID);

    assertThrows(RestaurantNotFountException.class, () -> service.remove(VALID_ID));
  }

  @Test
  void shouldThrowRestaurantHasDependents_whenTryRemoveWhileRestaurantHasDependents() {
    doThrow(DataIntegrityViolationException.class).when(repository).deleteById(VALID_ID);

    assertThrows(RestaurantHasDependents.class, () -> service.remove(VALID_ID));
  }

  @Test
  void shouldReturnRegistered_whenTryRegisterValidRestaurant() {
    Restaurant valid = aRestaurant().build();
    Restaurant registered = aRestaurant().withValidId().build();

    when(cuisineRegisterService.tryFetch(valid.getCuisine().getId())).thenReturn(valid.getCuisine());
    when(cityRegisterService.tryFetch(valid.getAddres().getCity().getId())).thenReturn(valid.getAddres().getCity());
    when(repository.save(valid)).thenReturn(registered);

    Restaurant actual = service.register(valid);
    assertThat(actual, isRestaurantEqualTo(registered));
  }

  @Test
  void shouldThrowRestaurantNotFound_whenTryUpdateWithUnregisteredRestaurantId() {
    Restaurant registered = aRestaurant().withValidId().build();
    when(repository.findById(VALID_ID)).thenReturn(Optional.empty());

    assertThrows(RestaurantNotFountException.class, () -> service.update(VALID_ID, registered));
  }

  @Test
  void shouldReturnRegistered_whenUpdateValidRestaurant() {
    Restaurant valid = aRestaurant().build();
    Restaurant registered = aRestaurant().withValidId().build();

    when(repository.findById(VALID_ID)).thenReturn(Optional.of(registered));

    when(repository.save(registered)).thenReturn(registered);

    Restaurant actual = service.update(VALID_ID, valid);
    assertThat(actual, isRestaurantEqualTo(registered));
  }

  @Test
  void shouldRetunRegistered_whenFetchWithValidRestaurantId() {
    Restaurant registered = aRestaurant().withValidId().build();

    when(repository.findById(VALID_ID)).thenReturn(Optional.of(registered));

    Restaurant actual = service.tryFetch(VALID_ID);
    assertThat(actual, isRestaurantEqualTo(registered));
  }

  @Test
  void shouldThrowRestaurantNotFound_whenFetchWithInvalidRestaurantId() {

    when(repository.findById(VALID_ID)).thenReturn(Optional.empty());

    assertThrows(RestaurantNotFountException.class, () -> service.tryFetch(VALID_ID));
  }

  @Test
  void shouldReturnRestaurantList_whenFetcAll() {
    when(repository.findAll())
        .thenReturn(Collections.singletonList(aRestaurant().withValidId().build()));
    List<Restaurant> actual = service.fetchAll();
    assertThat(actual.size(), is(1));
  }

  @Test
  void shouldCallRepository_whenActiveRestaurant() {
    when(repository.findById(VALID_ID))
        .thenReturn(Optional.of(aRestaurant().disactived().withValidId().build()));
    service.active(VALID_ID);

    verify(repository,times(1)).findById(VALID_ID);
  }

  @Test
  void shouldCallRestaurantActive_whenActiveRestaurant() {
    Restaurant mockRestaurant = mock(Restaurant.class);
    when(repository.findById(VALID_ID))
        .thenReturn(Optional.of(mockRestaurant));

    service.active(VALID_ID);

    verify(mockRestaurant, times(1)).active();
  }

  @Test
  void shouldCallRestaurantDisactive_whenDisactiveRestaurant() {
    Restaurant mockRestaurant = mock(Restaurant.class);
    when(repository.findById(VALID_ID))
        .thenReturn(Optional.of(mockRestaurant));

    service.disactive(VALID_ID);

    verify(mockRestaurant, times(1)).disactive();
  }
}
