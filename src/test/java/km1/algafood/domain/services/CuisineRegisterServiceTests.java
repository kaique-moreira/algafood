package km1.algafood.domain.services;

import static km1.algafood.utils.CuisineBuilderFactory.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static km1.algafood.matchers.CuisineMatcher.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import km1.algafood.domain.exceptions.CuisineHasDependents;
import km1.algafood.domain.exceptions.CuisineNotFountException;
import km1.algafood.domain.models.Cuisine;
import km1.algafood.domain.repositories.CuisineRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

@ExtendWith(MockitoExtension.class)
public class CuisineRegisterServiceTests {

  @Mock CuisineRepository repository;

  @InjectMocks CuisineRegisterService service;

  @Test
  void shouldThrowCuisineNotFoundException_whenTryFetchWithUnregisteredCuisineID() {
    when(repository.findById(1l)).thenReturn(Optional.empty());

    assertThrows(
      CuisineNotFountException.class, 
      () -> service.fetchByID(1l)
    );
  }

  @Test
  void shouldThrowCuisineNotFoundException_whenTryRemoveWithUnregisteredCuisineID() {
    doThrow(
      EmptyResultDataAccessException.class
    ).when(repository).deleteById(1l);

    assertThrows(
      CuisineNotFountException.class, 
      () -> service.remove(1l)
    );
  }

  @Test
  void shouldThrowCuisineHasDependents_whenTryRemoveWhileCuisineHasDependents() {
    doThrow(DataIntegrityViolationException.class)
      .when(repository).deleteById(1l);

    assertThrows(
      CuisineHasDependents.class,
    () -> service.remove(1l)
    );
  }

  @Test
  void shouldReturnRegistered_whenTryRegisterValidCuisine() {
    Cuisine valid = validCuisine().build();
    Cuisine registered = registeredCuisine().build();

    when(repository.save(valid))
      .thenReturn(registered);
    
    Cuisine actual = service.register(valid);
    assertThat(actual, isCuisineEqualTo(registered));
  }

  @Test
  void shouldThrowCuisineNotFound_whenTryUpdateWithUnregisteredCuisineId() {
    Cuisine registered = registeredCuisine().build();
    when(repository.findById(1l)).thenReturn(Optional.empty());

    assertThrows(
      CuisineNotFountException.class,
    () -> service.update(1l, registered)
    );
  }

  @Test
  void shouldReturnRegistered_whenUpdateValidCuisine() {
    Cuisine valid = validCuisine().build();
    Cuisine registered = registeredCuisine().build();
    
    when(repository.findById(1l))
      .thenReturn(Optional.of(registered));

    when(repository.save(registered))
      .thenReturn(registered);
    
    Cuisine actual = service.update(1l, valid);
    assertThat(actual, isCuisineEqualTo(registered));
  }

  @Test
  void shouldRetunRegisterd_whenFetchWithValidCuisineId(){
    Cuisine registered = registeredCuisine().build();

    when(repository.findById(1l))
      .thenReturn(Optional.of(registered));

    Cuisine actual = service.fetchByID(1l);
    assertThat(actual, isCuisineEqualTo(registered));
  }

  @Test
  void shouldThrowCuisineNotFound_whenFetchWithInvalidCuisineId(){

    when(repository.findById(1l)).thenReturn(Optional.empty());

      assertThrows(CuisineNotFountException.class, 
        () -> service.fetchByID(1l)
      );
  }

  @Test
  void shouldReturnCuisineList_whenFetcAll(){
    when(repository.findAll()).thenReturn(Collections.singletonList(registeredCuisine().build()));
    List<Cuisine> actual = service.fetchAll();
    assertThat(actual.size(), is(1));
  }
}
