package km1.algafood.api.assemblers;

import java.util.function.Function;
import km1.algafood.api.models.CityInput;
import km1.algafood.domain.models.City;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

@AllArgsConstructor
public class CityInputDisassembler implements Function<City, CityInput> {

  private final ModelMapper modelMapper;

  @Override
  public CityInput apply(City t) {
    return modelMapper.map(t, CityInput.class);
  }
}
