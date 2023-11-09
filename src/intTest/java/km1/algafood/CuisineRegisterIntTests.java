package km1.algafood;

import static km1.algafood.utils.CuisineBuilderFactory.validCuisine;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import km1.algafood.domain.exceptions.CuisineNotFountException;
import km1.algafood.domain.exceptions.DomainException;
import km1.algafood.domain.models.Cuisine;
import km1.algafood.domain.repositories.CuisineRepository;
import km1.algafood.domain.services.CuisineRegisterService;

@SpringBootTest
@ActiveProfiles("test")
public class CuisineRegisterIntTests {

  @Autowired private CuisineRegisterService registerService;
  @Autowired private CuisineRepository repository;

  @BeforeEach
  void setup() {
  }

  @Test
  @Sql({"/querys/truncate_tables.sql", "/querys/test_data.sql"})
  void shouldRegisterCuisine_whenTryRegisterValidCuisine() {
    Cuisine cuisine  = new Cuisine(null, "teest",Collections.emptyList());

    cuisine = repository.save(cuisine);

    // assertThat(cuisine).isNotNull();
  }

  @Test
  @Sql({"/querys/truncate_tables.sql", "/querys/test_data.sql"})
  void shouldThrowDomainException_whenTryRegisterCuisineWithNullName() {
    Cuisine cuisine = validCuisine().name(null).build();

    assertThrows(DomainException.class, () -> registerService.register(cuisine));
  }

  @Test
  @Sql({"/querys/truncate_tables.sql", "/querys/test_data.sql"})
  void shouldThrowCuisineHasDependents_whenTryRemoveCuisineWhileHasDependents() {
    // assertThrows(DomainException.class, () -> registerService.remove(1l));
  }

  @Test
  @Sql({"/querys/truncate_tables.sql", "/querys/test_data.sql"})
  void shouldThrowCuisineNotFound_whenTryRemoveUnregisteredCuisine() {
    assertThrows(CuisineNotFountException.class, () -> registerService.remove(100l));
  }

  @Test
  @Sql({"/querys/truncate_tables.sql", "/querys/test_data.sql"})
  void shouldThrowDomainException_whenTryUpdateCuisineWithNullName() {
    var registered = repository.findFirstByOrderById().get();
    registered.setName(null);
    assertThrows(DomainException.class, () -> registerService.update(registered.getId(),registered));
  }
}
