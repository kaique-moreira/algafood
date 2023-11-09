package km1.algafood.api.assemblers;

import java.util.function.Function;
import km1.algafood.api.models.CityDto;
import km1.algafood.domain.models.City;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

@AllArgsConstructor
public class CityDtoAssembler implements Function<City, CityDto> {

  private final ModelMapper modelMapper;

  @Override
  public CityDto apply(City t) {
    return modelMapper.map(t, CityDto.class);
  }
}
