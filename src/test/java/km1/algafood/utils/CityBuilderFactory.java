package km1.algafood.utils;

import km1.algafood.domain.models.City;
import km1.algafood.domain.models.City.CityBuilder;

public class CityBuilderFactory {
  public static CityBuilder validCity(){
    return City.builder().id(null).name("").state(null);
  }
  public static CityBuilder registeredCity(){
    return City.builder().id(null).name("").state(null);
  }
}
