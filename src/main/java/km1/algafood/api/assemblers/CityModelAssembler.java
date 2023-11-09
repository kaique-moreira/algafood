package km1.algafood.api.assemblers;

import java.util.function.Function;
import km1.algafood.api.models.CityModel;
import km1.algafood.domain.models.City;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CityModelAssembler implements Function<City, CityModel> {

  private final ModelMapper modelMapper;

  @Override
  public CityModel apply(City t) {
    return modelMapper.map(t, CityModel.class);
  }
}
