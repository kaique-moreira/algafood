package km1.algafood.domain.services;

import static km1.algafood.matchers.PaymentMethodMatcher.isPaymentMethodEqualTo;
import static km1.algafood.utils.PaymentMethodTestBuilder.*;
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

import km1.algafood.domain.exceptions.PaymentMethodHasDependents;
import km1.algafood.domain.exceptions.PaymentMethodNotFountException;
import km1.algafood.domain.models.PaymentMethod;
import km1.algafood.domain.repositories.PaymentMethodRepository;

@ExtendWith(MockitoExtension.class)
public class PaymentMethodRegisterServiceTests {

  private static final long INVALID_ID = 1l;

  private static final long VALID_ID = 1l;

  @Mock PaymentMethodRepository repository;

  @InjectMocks PaymentMethodRegisterService service;

  @Test
  void shouldThrowPaymentMethodNotFoundException_whenTryFetchWithUnregisteredPaymentMethodID() {
    when(repository.findById(INVALID_ID)).thenReturn(Optional.empty());
    assertThrows(
      PaymentMethodNotFountException.class, 
      () -> service.fetchByID(INVALID_ID)
    );
  }

  @Test
  void shouldThrowPaymentMethodNotFoundException_whenTryRemoveWithUnregisteredPaymentMethodID() {
    doThrow(
      EmptyResultDataAccessException.class
    ).when(repository).deleteById(INVALID_ID);

    assertThrows(
      PaymentMethodNotFountException.class, 
      () -> service.remove(INVALID_ID)
    );
  }

  @Test
  void shouldThrowPaymentMethodHasDependents_whenTryRemoveWhilePaymentMethodHasDependents() {
    doThrow(DataIntegrityViolationException.class)
      .when(repository).deleteById(VALID_ID);

    assertThrows(
      PaymentMethodHasDependents.class,
    () -> service.remove(VALID_ID)
    );
  }

  @Test
  void shouldReturnRegistered_whenTryRegisterValidPaymentMethod() {
    PaymentMethod valid = aPaymentMethod().build() ;
    PaymentMethod registered = aPaymentMethod().withValidId().build();

    when(repository.save(valid))
      .thenReturn(registered);
    
    PaymentMethod actual = service.register(valid);
    assertThat(actual, isPaymentMethodEqualTo(registered));
  }

  @Test
  void shouldThrowPaymentMethodNotFound_whenTryUpdateWithUnregisteredPaymentMethodId() {
    PaymentMethod registered = aPaymentMethod().withValidId().build();
    when(repository.findById(INVALID_ID)).thenReturn(Optional.empty());

    assertThrows(
      PaymentMethodNotFountException.class,
    () -> service.update(INVALID_ID, registered)
    );
  }

  @Test
  void shouldReturnRegistered_whenUpdateValidPaymentMethod() {
    PaymentMethod valid = aPaymentMethod().build();
    PaymentMethod registered = aPaymentMethod().withValidId().build();
    
    when(repository.findById(VALID_ID))
      .thenReturn(Optional.of(registered));

    when(repository.save(registered))
      .thenReturn(registered);
    
    PaymentMethod actual = service.update(VALID_ID, valid);
    assertThat(actual, isPaymentMethodEqualTo(registered));
  }

  @Test
  void shouldRetunRegistered_whenFetchWithValidPaymentMethodId(){
    PaymentMethod registered = aPaymentMethod().withValidId().build();

    when(repository.findById(VALID_ID))
      .thenReturn(Optional.of(registered));

    PaymentMethod actual = service.fetchByID(VALID_ID);
    assertThat(actual, isPaymentMethodEqualTo(registered));
  }

  @Test
  void shouldThrowPaymentMethodNotFound_whenFetchWithInvalidPaymentMethodId(){

    when(repository.findById(VALID_ID)).thenReturn(Optional.empty());

      assertThrows(PaymentMethodNotFountException.class, 
        () -> service.fetchByID(VALID_ID)
      );
  }

  @Test
  void shouldReturnPaymentMethodList_whenFetcAll(){
    PaymentMethod registered = aPaymentMethod().withValidId().build();
    when(repository.findAll()).thenReturn(Collections.singletonList(registered));
    List<PaymentMethod> actual = service.fetchAll();
    assertThat(actual.size(), is(1));
  }
}
