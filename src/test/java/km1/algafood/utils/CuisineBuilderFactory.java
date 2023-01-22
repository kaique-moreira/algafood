package km1.algafood.utils;

import km1.algafood.domain.models.Cuisine;
import km1.algafood.domain.models.Cuisine.CuisineBuilder;

public class CuisineBuilderFactory {
  public static CuisineBuilder validCuisine(){
    return Cuisine.builder().name("test");
  }
  public static CuisineBuilder registeredCuisine(){
    return Cuisine.builder().id(1l).name("").restaurants(null);
  }
}
