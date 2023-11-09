package km1.algafood.domain.services;

import static km1.algafood.matchers.CuisineMatcher.isCuisineEqualTo;
import static km1.algafood.utils.CuisineTestBuilder.*;
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

import km1.algafood.domain.exceptions.CuisineHasDependents;
import km1.algafood.domain.exceptions.CuisineNotFountException;
import km1.algafood.domain.models.Cuisine;
import km1.algafood.domain.repositories.CuisineRepository;

@ExtendWith(MockitoExtension.class)
public class CuisineRegisterServiceTests {

  private static final long INVALID_ID = 1l;

  private static final long VALID_ID = 1l;

  @Mock CuisineRepository repository;

  @InjectMocks CuisineRegisterService service;

  @Test
  void shouldThrowCuisineNotFoundException_whenTryFetchWithUnregisteredCuisineID() {
    when(repository.findById(INVALID_ID)).thenReturn(Optional.empty());
    assertThrows(
      CuisineNotFountException.class, 
      () -> service.fetchByID(INVALID_ID)
    );
  }

  @Test
  void shouldThrowCuisineNotFoundException_whenTryRemoveWithUnregisteredCuisineID() {
    doThrow(
      EmptyResultDataAccessException.class
    ).when(repository).deleteById(INVALID_ID);

    assertThrows(
      CuisineNotFountException.class, 
      () -> service.remove(INVALID_ID)
    );
  }

  @Test
  void shouldThrowCuisineHasDependents_whenTryRemoveWhileCuisineHasDependents() {
    doThrow(DataIntegrityViolationException.class)
      .when(repository).deleteById(VALID_ID);

    assertThrows(
      CuisineHasDependents.class,
    () -> service.remove(VALID_ID)
    );
  }

  @Test
  void shouldReturnRegistered_whenTryRegisterValidCuisine() {
    Cuisine valid = aCuisine().build() ;
    Cuisine registered = aCuisine().withValidId().build();

    when(repository.save(valid))
      .thenReturn(registered);
    
    Cuisine actual = service.register(valid);
    assertThat(actual, isCuisineEqualTo(registered));
  }

  @Test
  void shouldThrowCuisineNotFound_whenTryUpdateWithUnregisteredCuisineId() {
    Cuisine registered = aCuisine().withValidId().build();
    when(repository.findById(INVALID_ID)).thenReturn(Optional.empty());

    assertThrows(
      CuisineNotFountException.class,
    () -> service.update(INVALID_ID, registered)
    );
  }

  @Test
  void shouldReturnRegistered_whenUpdateValidCuisine() {
    Cuisine valid = aCuisine().build();
    Cuisine registered = aCuisine().withValidId().build();
    
    when(repository.findById(VALID_ID))
      .thenReturn(Optional.of(registered));

    when(repository.save(registered))
      .thenReturn(registered);
    
    Cuisine actual = service.update(VALID_ID, valid);
    assertThat(actual, isCuisineEqualTo(registered));
  }

  @Test
  void shouldRetunRegistered_whenFetchWithValidCuisineId(){
    Cuisine registered = aCuisine().withValidId().build();

    when(repository.findById(VALID_ID))
      .thenReturn(Optional.of(registered));

    Cuisine actual = service.fetchByID(VALID_ID);
    assertThat(actual, isCuisineEqualTo(registered));
  }

  @Test
  void shouldThrowCuisineNotFound_whenFetchWithInvalidCuisineId(){

    when(repository.findById(VALID_ID)).thenReturn(Optional.empty());

      assertThrows(CuisineNotFountException.class, 
        () -> service.fetchByID(VALID_ID)
      );
  }

  @Test
  void shouldReturnCuisineList_whenFetcAll(){
    Cuisine registered = aCuisine().withValidId().build();
    when(repository.findAll()).thenReturn(Collections.singletonList(registered));
    List<Cuisine> actual = service.fetchAll();
    assertThat(actual.size(), is(1));
  }
}
