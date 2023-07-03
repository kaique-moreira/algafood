package km1.algafood.domain.services;

import static km1.algafood.matchers.UserMatcher.isUserEqualTo;
import static km1.algafood.utils.UserTestBuilder.aUser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import km1.algafood.domain.exceptions.UserHasDependents;
import km1.algafood.domain.exceptions.UserNotFountException;
import km1.algafood.domain.models.User;
import km1.algafood.domain.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

@ExtendWith(MockitoExtension.class)
public class UserRegisterServiceTests {

  private static final long INVALID_ID = 1l;

  private static final long VALID_ID = 1l;

  @Mock UserRepository repository;

  @InjectMocks UserRegisterService service;

  @Test
  void shouldThrowUserNotFoundException_whenTryFetchWithUnregisteredUserID() {
    when(repository.findById(INVALID_ID)).thenReturn(Optional.empty());
    assertThrows(UserNotFountException.class, () -> service.fetchByID(INVALID_ID));
  }

  @Test
  void shouldThrowUserNotFoundException_whenTryRemoveWithUnregisteredUserID() {
    doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(INVALID_ID);

    assertThrows(UserNotFountException.class, () -> service.remove(INVALID_ID));
  }

  @Test
  void shouldThrowUserHasDependents_whenTryRemoveWhileUserHasDependents() {
    doThrow(DataIntegrityViolationException.class).when(repository).deleteById(VALID_ID);

    assertThrows(UserHasDependents.class, () -> service.remove(VALID_ID));
  }

  @Test
  void shouldReturnRegistered_whenTryRegisterValidUser() {
    User valid = aUser().build();
    User registered = aUser().withValidId().build();

    when(repository.save(valid)).thenReturn(registered);

    User actual = service.register(valid);
    assertThat(actual, isUserEqualTo(registered));
  }

  @Test
  void shouldThrowUserNotFound_whenTryUpdateWithUnregisteredUserId() {
    User registered = aUser().withValidId().build();
    when(repository.findById(INVALID_ID)).thenReturn(Optional.empty());

    assertThrows(UserNotFountException.class, () -> service.update(INVALID_ID, registered));
  }

  @Test
  void shouldReturnRegistered_whenUpdateValidUser() {
    User valid = aUser().build();
    User registered = aUser().withValidId().build();

    when(repository.findById(VALID_ID)).thenReturn(Optional.of(registered));

    when(repository.save(registered)).thenReturn(registered);

    User actual = service.update(VALID_ID, valid);
    assertThat(actual, isUserEqualTo(registered));
  }

  @Test
  void shouldRetunRegistered_whenFetchWithValidUserId() {
    User registered = aUser().withValidId().build();

    when(repository.findById(VALID_ID)).thenReturn(Optional.of(registered));

    User actual = service.fetchByID(VALID_ID);
    assertThat(actual, isUserEqualTo(registered));
  }

  @Test
  void shouldThrowUserNotFound_whenFetchWithInvalidUserId() {

    when(repository.findById(VALID_ID)).thenReturn(Optional.empty());

    assertThrows(UserNotFountException.class, () -> service.fetchByID(VALID_ID));
  }

  @Test
  void shouldReturnUserList_whenFetcAll() {
    User registered = aUser().withValidId().build();
    when(repository.findAll()).thenReturn(Collections.singletonList(registered));
    List<User> actual = service.fetchAll();
    assertThat(actual.size(), is(1));
  }
}
