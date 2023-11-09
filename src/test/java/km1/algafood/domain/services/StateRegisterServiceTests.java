package km1.algafood.domain.services;

import static km1.algafood.matchers.StateMatcher.isStateEqualTo;
import static km1.algafood.utils.StateTestBuilder.*;
import static km1.algafood.utils.StateTestBuilder.aState;
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

import km1.algafood.domain.exceptions.StateHasDependents;
import km1.algafood.domain.exceptions.StateNotFountException;
import km1.algafood.domain.models.State;
import km1.algafood.domain.repositories.StateRepository;

@ExtendWith(MockitoExtension.class)
public class StateRegisterServiceTests {

  @Mock StateRepository repository;

  @InjectMocks StateRegisterService service;

  @Test
  void shouldThrowStateNotFoundException_whenTryFetchWithUnregisteredStateID() {
    when(repository.findById(1l)).thenReturn(Optional.empty());

    assertThrows(
      StateNotFountException.class, 
      () -> service.fetchByID(1l)
    );
  }

  @Test
  void shouldThrowStateNotFoundException_whenTryRemoveWithUnregisteredStateID() {
    doThrow(
      EmptyResultDataAccessException.class
    ).when(repository).deleteById(1l);

    assertThrows(
      StateNotFountException.class, 
      () -> service.remove(1l)
    );
  }

  @Test
  void shouldThrowStateHasDependents_whenTryRemoveWhileStateHasDependents() {
    doThrow(DataIntegrityViolationException.class)
      .when(repository).deleteById(1l);

    assertThrows(
      StateHasDependents.class,
    () -> service.remove(1l)
    );
  }

  @Test
  void shouldReturnRegistered_whenTryRegisterValidState() {
    State valid = aState().build();
    State registered = aState().withValidId().build();

    when(repository.save(valid))
      .thenReturn(registered);
    
    State actual = service.register(valid);
    assertThat(actual, isStateEqualTo(registered));
  }

  @Test
  void shouldThrowStateNotFound_whenTryUpdateWithUnregisteredStateId() {
    State registered = aState().withValidId().build();
    when(repository.findById(1l)).thenReturn(Optional.empty());

    assertThrows(
      StateNotFountException.class,
    () -> service.update(1l, registered)
    );
  }

  @Test
  void shouldReturnRegistered_whenUpdateValidState() {
    State valid = aState().build();
    State registered = aState().withValidId().build();
    
    when(repository.findById(1l))
      .thenReturn(Optional.of(registered));

    when(repository.save(registered))
      .thenReturn(registered);
    
    State actual = service.update(1l, valid);
    assertThat(actual, isStateEqualTo(registered));
  }

  @Test
  void shouldRetunRegistered_whenFetchWithValidStateId(){
    State registered = aState().withValidId().build();

    when(repository.findById(1l))
      .thenReturn(Optional.of(registered));

    State actual = service.fetchByID(1l);
    assertThat(actual, isStateEqualTo(registered));
  }

  @Test
  void shouldThrowStateNotFound_whenFetchWithInvalidStateId(){

    when(repository.findById(1l)).thenReturn(Optional.empty());

      assertThrows(StateNotFountException.class, 
        () -> service.fetchByID(1l)
      );
  }

  @Test
  void shouldReturnStateList_whenFetcAll(){
    State registered = aState().withValidId().build();
    when(repository.findAll()).thenReturn(Collections.singletonList(registered));
    List<State> actual = service.fetchAll();
    assertThat(actual.size(), is(1));
  }
}
