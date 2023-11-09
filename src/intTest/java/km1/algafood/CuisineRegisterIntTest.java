package km1.algafood;

import static km1.algafood.utils.CuisineBuilderFactory.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import km1.algafood.domain.exceptions.CuisineNotFountException;
import km1.algafood.domain.exceptions.DomainException;
import km1.algafood.domain.models.Cuisine;
import km1.algafood.domain.services.CuisineRegisterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CuisineRegisterIntTest {

  @Autowired private CuisineRegisterService registerService;

  @Test
  void shouldRegisterCuisine_whenTryRegisterValidCuisine() {
    Cuisine cuisine = validCuisine().build();

    cuisine = registerService.register(cuisine);

    assertThat(cuisine).isNotNull();
  }

  @Test
  void shouldThrowDomainException_whenTryRegisterCuisineWithNullName() {
    Cuisine cuisine = validCuisine().name(null).build();

    assertThrows(DomainException.class, () -> registerService.register(cuisine));
  }

  @Test
  void shouldThrowCuisineHasDependents_whenTryRemoveCuisineWhileHasDependents() {
    assertThrows(DomainException.class, () -> registerService.remove(1l));
  }

  @Test
  void shouldThrowCuisineNotFound_whenTryRemoveUnregisteredCuisine() {
    assertThrows(CuisineNotFountException.class, () -> registerService.remove(100l));
  }

 @Test
  void shouldThrowDomainException_whenTryUpdateCuisineWithNullName() {
    Cuisine cuisine = validCuisine().build();
    Cuisine registered = registerService.register(cuisine);
    registered.setName(null);
    assertThrows(DomainException.class, () -> registerService.update(registered.getId(),registered));
  }
}
