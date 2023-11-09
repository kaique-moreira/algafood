package km1.algafood.domain.services;

import static km1.algafood.matchers.RestaurantMatcher.isRestaurantEqualTo;
import static km1.algafood.utils.RestaurantBuilderFactory.registeredRestaurant;
import static km1.algafood.utils.RestaurantBuilderFactory.validRestaurant;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import km1.algafood.domain.exceptions.RestaurantHasDependents;
import km1.algafood.domain.exceptions.RestaurantNotFountException;
import km1.algafood.domain.models.Restaurant;
import km1.algafood.domain.repositories.RestaurantRepository;

@ExtendWith(MockitoExtension.class)
public class RestaurantRegisterServiceTests {

  @Mock RestaurantRepository repository;

  @InjectMocks RestaurantRegisterService service;

  @Test
  void shouldThrowRestaurantNotFoundException_whenTryFetchWithUnregisteredRestaurantID() {
    when(repository.findById(1l)).thenReturn(Optional.empty());

    assertThrows(
      RestaurantNotFountException.class, 
      () -> service.fetchByID(1l)
    );
  }

  @Test
  void shouldThrowRestaurantNotFoundException_whenTryRemoveWithUnregisteredRestaurantID() {
    doThrow(
      EmptyResultDataAccessException.class
    ).when(repository).deleteById(1l);

    assertThrows(
      RestaurantNotFountException.class, 
      () -> service.remove(1l)
    );
  }

  @Test
  void shouldThrowRestaurantHasDependents_whenTryRemoveWhileRestaurantHasDependents() {
    doThrow(DataIntegrityViolationException.class)
      .when(repository).deleteById(1l);

    assertThrows(
      RestaurantHasDependents.class,
    () -> service.remove(1l)
    );
  }

  @Test
  void shouldReturnRegistered_whenTryRegisterValidRestaurant() {
    Restaurant valid = validRestaurant().build();
    Restaurant registered = registeredRestaurant().build();

    when(repository.save(valid))
      .thenReturn(registered);
    
    Restaurant actual = service.register(valid);
    assertThat(actual, isRestaurantEqualTo(registered));
  }

  @Test
  void shouldThrowRestaurantNotFound_whenTryUpdateWithUnregisteredRestaurantId() {
    Restaurant registered = registeredRestaurant().build();
    when(repository.findById(1l)).thenReturn(Optional.empty());

    assertThrows(
      RestaurantNotFountException.class,
    () -> service.update(1l, registered)
    );
  }

  @Test
  void shouldReturnRegistered_whenUpdateValidRestaurant() {
    Restaurant valid = validRestaurant().build();
    Restaurant registered = registeredRestaurant().build();
    
    when(repository.findById(1l))
      .thenReturn(Optional.of(registered));

    when(repository.save(registered))
      .thenReturn(registered);
    
    Restaurant actual = service.update(1l, valid);
    assertThat(actual, isRestaurantEqualTo(registered));
  }

  @Test
  void shouldRetunRegistered_whenFetchWithValidRestaurantId(){
    Restaurant registered = registeredRestaurant().build();

    when(repository.findById(1l))
      .thenReturn(Optional.of(registered));

    Restaurant actual = service.fetchByID(1l);
    assertThat(actual, isRestaurantEqualTo(registered));
  }

  @Test
  void shouldThrowRestaurantNotFound_whenFetchWithInvalidRestaurantId(){

    when(repository.findById(1l)).thenReturn(Optional.empty());

      assertThrows(RestaurantNotFountException.class, 
        () -> service.fetchByID(1l)
      );
  }

  @Test
  void shouldReturnRestaurantList_whenFetcAll(){
    when(repository.findAll()).thenReturn(Collections.singletonList(registeredRestaurant().build()));
    List<Restaurant> actual = service.fetchAll();
    assertThat(actual.size(), is(1));
  }
}
