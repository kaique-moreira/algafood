package km1.algafood;

import static org.assertj.core.api.Assertions.*;
import static km1.algafood.utils.CuisineBuilderFactory.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import km1.algafood.domain.models.Cuisine;
import km1.algafood.domain.services.CuisineRegisterService;

@SpringBootTest
public class CuisineRegisterIntTest {

  @Autowired
  private  CuisineRegisterService registerService;

  
  @Test
  void shouldRegisterCuisine_whenTryRegisterValidCuisine(){
    Cuisine cuisine = validCuisine().build();

    cuisine = registerService.register(cuisine);

    assertThat(cuisine).isNotNull();
  }

@Test
  void shoulRegisterCuisine_whenTryRegisterValidCuisine(){
    Cuisine cuisine = validCuisine().name(null).build();

    cuisine = registerService.register(cuisine);

    assertThat(cuisine).isNotNull();
  }
  
}
