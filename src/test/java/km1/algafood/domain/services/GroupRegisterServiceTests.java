package km1.algafood.domain.services;

import static km1.algafood.matchers.GroupMatcher.isGroupEqualTo;
import static km1.algafood.utils.GroupTestBuilder.*;
import static km1.algafood.utils.GroupTestBuilder.aGroup;
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

import km1.algafood.domain.exceptions.GroupHasDependents;
import km1.algafood.domain.exceptions.GroupNotFountException;
import km1.algafood.domain.models.Group;
import km1.algafood.domain.repositories.GroupRepository;

@ExtendWith(MockitoExtension.class)
public class GroupRegisterServiceTests {

  @Mock GroupRepository repository;

  @InjectMocks GroupRegisterService service;

  @Test
  void shouldThrowGroupNotFoundException_whenTryFetchWithUnregisteredGroupID() {
    when(repository.findById(1l)).thenReturn(Optional.empty());

    assertThrows(
      GroupNotFountException.class, 
      () -> service.fetchByID(1l)
    );
  }

  @Test
  void shouldThrowGroupNotFoundException_whenTryRemoveWithUnregisteredGroupID() {
    doThrow(
      EmptyResultDataAccessException.class
    ).when(repository).deleteById(1l);

    assertThrows(
      GroupNotFountException.class, 
      () -> service.remove(1l)
    );
  }

  @Test
  void shouldThrowGroupHasDependents_whenTryRemoveWhileGroupHasDependents() {
    doThrow(DataIntegrityViolationException.class)
      .when(repository).deleteById(1l);

    assertThrows(
      GroupHasDependents.class,
    () -> service.remove(1l)
    );
  }

  @Test
  void shouldReturnRegistered_whenTryRegisterValidGroup() {
    Group valid = aGroup().build();
    Group registered = aGroup().withValidId().build();

    when(repository.save(valid))
      .thenReturn(registered);
    
    Group actual = service.register(valid);
    assertThat(actual, isGroupEqualTo(registered));
  }

  @Test
  void shouldThrowGroupNotFound_whenTryUpdateWithUnregisteredGroupId() {
    Group registered = aGroup().withValidId().build();
    when(repository.findById(1l)).thenReturn(Optional.empty());

    assertThrows(
      GroupNotFountException.class,
    () -> service.update(1l, registered)
    );
  }

  @Test
  void shouldReturnRegistered_whenUpdateValidGroup() {
    Group valid = aGroup().build();
    Group registered = aGroup().withValidId().build();
    
    when(repository.findById(1l))
      .thenReturn(Optional.of(registered));

    when(repository.save(valid))
      .thenReturn(registered);
    
    Group actual = service.update(1l, valid);
    assertThat(actual, isGroupEqualTo(registered));
  }

  @Test
  void shouldRetunRegistered_whenFetchWithValidGroupId(){
    Group registered = aGroup().withValidId().build();

    when(repository.findById(1l))
      .thenReturn(Optional.of(registered));

    Group actual = service.fetchByID(1l);
    assertThat(actual, isGroupEqualTo(registered));
  }

  @Test
  void shouldThrowGroupNotFound_whenFetchWithInvalidGroupId(){

    when(repository.findById(1l)).thenReturn(Optional.empty());

      assertThrows(GroupNotFountException.class, 
        () -> service.fetchByID(1l)
      );
  }

  @Test
  void shouldReturnGroupList_whenFetcAll(){
    Group registered = aGroup().withValidId().build();
    when(repository.findAll()).thenReturn(Collections.singletonList(registered));
    List<Group> actual = service.fetchAll();
    assertThat(actual.size(), is(1));
  }
}
