package km1.algafood;

import static km1.algafood.utils.StateTestBuilder.aState;
import static org.junit.jupiter.api.Assertions.assertThrows;


import static  org.hamcrest.MatcherAssert.*;
import static  org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import km1.algafood.domain.exceptions.StateNotFountException;
import km1.algafood.domain.exceptions.DomainException;
import km1.algafood.domain.models.State;
import km1.algafood.domain.services.StateRegisterService;

@SpringBootTest
@ActiveProfiles("test")
public class StateRegisterIntTests {

  private static final String TEST_DATA = "/querys/test_data.sql";
  private static final String TRUNCATE_TABLES = "/querys/truncate_tables.sql";
  private static final long WITHOUT_DEPENDENTS_ID = 3l;
  private static final long INVALID_ID = 100l;
  private static final long VALID_ID = 1l;
  @Autowired private StateRegisterService underTest;

  @BeforeEach
  void setup() {
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldRegisterState_whenTryRegisterValidState() {
    State toRegister  = aState().build();
    State registered = underTest.register(toRegister);
    assertThat(registered.getId(), notNullValue());
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldReturnState_whenTryFetchValidState() {
    var cuisine = underTest.fetchByID(VALID_ID);
    assertThat(cuisine, notNullValue(State.class));
    assertThat(cuisine.getId(), is(VALID_ID));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldReturnAllStates_whenTryFetchAllStates() {
    var cuisines = underTest.fetchAll();
    assertThat(cuisines, notNullValue());
    assertThat(cuisines, hasSize(3));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldThrowStateNotFound_whenTryFetchValidState() {
    assertThrows(StateNotFountException.class, () -> underTest.fetchByID(INVALID_ID));
  }

  @Test @Sql({TRUNCATE_TABLES, TEST_DATA}) void shouldThrowDomainException_whenTryRegisterStateWithNullName() {
    State cuisine = aState().withNullName().build();
    assertThrows(DomainException.class, () -> underTest.register(cuisine));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldThrowStateHasDependents_whenTryRemoveStateWhileHasDependents() {
    assertThrows(DomainException.class, () -> underTest.remove(1l));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldThrowStateNotFound_whenTryRemoveUnregisteredState() {
    assertThrows(StateNotFountException.class, () -> underTest.remove(INVALID_ID));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldRemoveState_whenTryRemoveValidState() {
    underTest.remove(WITHOUT_DEPENDENTS_ID);
    assertThrows(StateNotFountException.class, () -> underTest.fetchByID(WITHOUT_DEPENDENTS_ID));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldThrowDomainException_whenTryUpdateStateWithNullName() {
    var toUpdate = aState().build();
    assertThrows(DomainException.class, () -> underTest.update(VALID_ID,toUpdate));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldThrowStateNotFoundException_whenTryUpdateUnregisteredState() {
    var toUpdate = aState().build();
    assertThrows(DomainException.class, () -> underTest.update(INVALID_ID,toUpdate));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldReturnUpdatedState_whenTryUpdateValidState() {
    var toUpdate = aState().build();
    var updated = underTest.update(VALID_ID, toUpdate);
    assertThat(updated.getName(),is(toUpdate.getName()));
    assertThat(updated.getId(),is(VALID_ID));
  }
}
