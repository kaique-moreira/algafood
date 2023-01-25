package km1.algafood.utils;

import km1.algafood.domain.models.Cuisine;
import km1.algafood.domain.models.Cuisine.CuisineBuilder;
import km1.algafood.api.models.CuisineDto;
import km1.algafood.api.models.CuisineInput;
import km1.algafood.api.models.CuisineDto.CuisineDtoBuilder;
import km1.algafood.api.models.CuisineInput.CuisineInputBuilder;

public class CuisineBuilderFactory {
  public static CuisineBuilder validCuisine(){
    return Cuisine.builder().name("francesa");
  }

  public static CuisineInputBuilder validCuisineInput(){
    return CuisineInput.builder().name("francesa");
  }

  public static CuisineDtoBuilder validCuisineDto(){
    return CuisineDto.builder().name("francesa");
  }

  public static CuisineBuilder registeredCuisine(){
    return Cuisine.builder().id(1l).name("francesa").restaurants(null);
  }
}
