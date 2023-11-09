package km1.algafood;

import static km1.algafood.utils.CuisineTestBuilder.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


import static  org.hamcrest.MatcherAssert.*;
import static  org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import km1.algafood.domain.exceptions.CuisineNotFountException;
import km1.algafood.domain.exceptions.DomainException;
import km1.algafood.domain.models.Cuisine;
import km1.algafood.domain.services.CuisineRegisterService;

@SpringBootTest
@ActiveProfiles("test")
public class CuisineRegisterIntTests {

  private static final String TEST_DATA = "/querys/test_data.sql";
  private static final String TRUNCATE_TABLES = "/querys/truncate_tables.sql";
  private static final long WITHOUT_DEPENDENTS_ID = 3l;
  private static final long INVALID_ID = 100l;
  private static final long VALID_ID = 1l;
  @Autowired private CuisineRegisterService underTest;

  @BeforeEach
  void setup() {
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldRegisterCuisine_whenTryRegisterValidCuisine() {
    Cuisine toRegister  = aCuisine().build();
    Cuisine registered = underTest.register(toRegister);
    assertThat(registered.getId(), notNullValue());
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldReturnCuisine_whenTryFetchValidCuisine() {
    var cuisine = underTest.fetchByID(VALID_ID);
    assertThat(cuisine, notNullValue(Cuisine.class));
    assertThat(cuisine.getId(), is(VALID_ID));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldReturnAllCuisines_whenTryFetchAllCuisines() {
    var cuisines = underTest.fetchAll();
    assertThat(cuisines, notNullValue());
    assertThat(cuisines, hasSize(3));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldThrowCuisineNotFound_whenTryFetchValidCuisine() {
    assertThrows(CuisineNotFountException.class, () -> underTest.fetchByID(INVALID_ID));
  }

  @Test @Sql({TRUNCATE_TABLES, TEST_DATA}) void shouldThrowDomainException_whenTryRegisterCuisineWithNullName() {
    Cuisine cuisine = aCuisine().withNullName().build();
    assertThrows(DomainException.class, () -> underTest.register(cuisine));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldThrowCuisineHasDependents_whenTryRemoveCuisineWhileHasDependents() {
    assertThrows(DomainException.class, () -> underTest.remove(1l));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldThrowCuisineNotFound_whenTryRemoveUnregisteredCuisine() {
    assertThrows(CuisineNotFountException.class, () -> underTest.remove(INVALID_ID));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldRemoveCuisine_whenTryRemoveValidCuisine() {
    underTest.remove(WITHOUT_DEPENDENTS_ID);
    assertThrows(CuisineNotFountException.class, () -> underTest.fetchByID(WITHOUT_DEPENDENTS_ID));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldThrowDomainException_whenTryUpdateCuisineWithNullName() {
    var toUpdate = aCuisine().withNullName().build();
    assertThrows(DomainException.class, () -> underTest.update(VALID_ID,toUpdate));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldThrowCuisineNotFoundException_whenTryUpdateUnregisteredCuisine() {
    var toUpdate = aCuisine().build();
    assertThrows(DomainException.class, () -> underTest.update(INVALID_ID,toUpdate));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldReturnUpdatedCuisine_whenTryUpdateValidCuisine() {
    var toUpdate = aCuisine().build();
    var updated = underTest.update(VALID_ID, toUpdate);
    assertThat(updated.getName(),is(toUpdate.getName()));
    assertThat(updated.getId(),is(VALID_ID));
  }
}
