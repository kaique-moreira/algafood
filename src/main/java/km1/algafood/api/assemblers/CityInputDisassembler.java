package km1.algafood.api.assemblers;

import java.util.function.Function;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import km1.algafood.api.models.CityInput;
import km1.algafood.domain.models.City;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CityInputDisassembler implements Function<CityInput, City> {

  private final ModelMapper modelMapper;

  @Override
  public City apply(CityInput cityInput) {
    return modelMapper.map(cityInput, City.class);
  }
}
