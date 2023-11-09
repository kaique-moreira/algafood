package km1.algafood;

import static km1.algafood.utils.CityBuilderFactory.validCity;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import km1.algafood.domain.exceptions.CityNotFountException;
import km1.algafood.domain.exceptions.DomainException;
import km1.algafood.domain.models.City;
import km1.algafood.domain.models.State;
import km1.algafood.domain.services.CityRegisterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@ActiveProfiles("test")
public class CityRegisterIntTests {

  private static final String TEST_DATA = "/querys/test_data.sql";
  private static final String TRUNCATE_TABLES = "/querys/truncate_tables.sql";
  private static final long WITHOUT_DEPENDENTS_ID = 3l;
  private static final long INVALID_ID = 100l;
  private static final long VALID_ID = 1l;
  @Autowired private CityRegisterService underTest;

  @BeforeEach
  void setup() {}

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldRegisterCity_whenTryRegisterValidCity() {
    City toRegister = City.builder().name("").state(State.builder().id(1l).build()).build();
    City registered = underTest.register(toRegister);
    assertThat(registered.getId(), notNullValue());
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldReturnCity_whenTryFetchValidCity() {
    var cuisine = underTest.fetchByID(VALID_ID);
    assertThat(cuisine, notNullValue(City.class));
    assertThat(cuisine.getId(), is(VALID_ID));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldReturnAllCitys_whenTryFetchAllCitys() {
    var cuisines = underTest.fetchAll();
    assertThat(cuisines, notNullValue());
    assertThat(cuisines, hasSize(3));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldThrowCityNotFound_whenTryFetchValidCity() {
    assertThrows(CityNotFountException.class, () -> underTest.fetchByID(INVALID_ID));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldThrowDomainException_whenTryRegisterCityWithNullName() {
    City cuisine = validCity().name(null).build();
    assertThrows(DomainException.class, () -> underTest.register(cuisine));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldThrowCityHasDependents_whenTryRemoveCityWhileHasDependents() {
    assertThrows(DomainException.class, () -> underTest.remove(1l));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldThrowCityNotFound_whenTryRemoveUnregisteredCity() {
    assertThrows(CityNotFountException.class, () -> underTest.remove(INVALID_ID));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldRemoveCity_whenTryRemoveValidCity() {
    underTest.remove(WITHOUT_DEPENDENTS_ID);
    assertThrows(CityNotFountException.class, () -> underTest.fetchByID(WITHOUT_DEPENDENTS_ID));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldThrowDomainException_whenTryUpdateCityWithNullName() {
    var toUpdate = City.builder().build();
    assertThrows(DomainException.class, () -> underTest.update(VALID_ID, toUpdate));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldThrowCityNotFoundException_whenTryUpdateUnregisteredCity() {
    var toUpdate = City.builder().build();
    assertThrows(DomainException.class, () -> underTest.update(INVALID_ID, toUpdate));
  }

  @Test
  @Sql({TRUNCATE_TABLES, TEST_DATA})
  void shouldReturnUpdatedCity_whenTryUpdateValidCity() {
    var toUpdate = City.builder().name("test").build();
    var updated = underTest.update(VALID_ID, toUpdate);
    assertThat(updated.getName(), is(toUpdate.getName()));
    assertThat(updated.getId(), is(VALID_ID));
  }
}
