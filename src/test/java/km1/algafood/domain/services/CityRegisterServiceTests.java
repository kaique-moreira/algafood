package km1.algafood.domain.services;

import static km1.algafood.matchers.CityMatcher.isCityEqualTo;
import static km1.algafood.utils.CityTestBuilder.*;
import static km1.algafood.utils.CityTestBuilder.aCity;
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

import km1.algafood.domain.exceptions.CityHasDependents;
import km1.algafood.domain.exceptions.CityNotFountException;
import km1.algafood.domain.models.City;
import km1.algafood.domain.repositories.CityRepository;

@ExtendWith(MockitoExtension.class)
public class CityRegisterServiceTests {

  @Mock CityRepository repository;

  @InjectMocks CityRegisterService service;

  @Test
  void shouldThrowCityNotFoundException_whenTryFetchWithUnregisteredCityID() {
    when(repository.findById(1l)).thenReturn(Optional.empty());

    assertThrows(
      CityNotFountException.class, 
      () -> service.fetchByID(1l)
    );
  }

  @Test
  void shouldThrowCityNotFoundException_whenTryRemoveWithUnregisteredCityID() {
    doThrow(
      EmptyResultDataAccessException.class
    ).when(repository).deleteById(1l);

    assertThrows(
      CityNotFountException.class, 
      () -> service.remove(1l)
    );
  }

  @Test
  void shouldThrowCityHasDependents_whenTryRemoveWhileCityHasDependents() {
    doThrow(DataIntegrityViolationException.class)
      .when(repository).deleteById(1l);

    assertThrows(
      CityHasDependents.class,
    () -> service.remove(1l)
    );
  }

  @Test
  void shouldReturnRegistered_whenTryRegisterValidCity() {
    City valid = aCity().build();
    City registered = aCity().withValidId().build();

    when(repository.save(valid))
      .thenReturn(registered);
    
    City actual = service.register(valid);
    assertThat(actual, isCityEqualTo(registered));
  }

  @Test
  void shouldThrowCityNotFound_whenTryUpdateWithUnregisteredCityId() {
    City registered = aCity().withValidId().build();
    when(repository.findById(1l)).thenReturn(Optional.empty());

    assertThrows(
      CityNotFountException.class,
    () -> service.update(1l, registered)
    );
  }

  @Test
  void shouldReturnRegistered_whenUpdateValidCity() {
    City valid = aCity().build();
    City registered = aCity().withValidId().build();
    
    when(repository.findById(1l))
      .thenReturn(Optional.of(registered));

    when(repository.save(valid))
      .thenReturn(registered);
    
    City actual = service.update(1l, valid);
    assertThat(actual, isCityEqualTo(registered));
  }

  @Test
  void shouldRetunRegistered_whenFetchWithValidCityId(){
    City registered = aCity().withValidId().build();

    when(repository.findById(1l))
      .thenReturn(Optional.of(registered));

    City actual = service.fetchByID(1l);
    assertThat(actual, isCityEqualTo(registered));
  }

  @Test
  void shouldThrowCityNotFound_whenFetchWithInvalidCityId(){

    when(repository.findById(1l)).thenReturn(Optional.empty());

      assertThrows(CityNotFountException.class, 
        () -> service.fetchByID(1l)
      );
  }

  @Test
  void shouldReturnCityList_whenFetcAll(){
    City registered = aCity().withValidId().build();
    when(repository.findAll()).thenReturn(Collections.singletonList(registered));
    List<City> actual = service.fetchAll();
    assertThat(actual.size(), is(1));
  }
}
