package km1.algafood.api.assemblers;

import java.util.function.Function;
import km1.algafood.api.models.RestaurantInput;
import km1.algafood.domain.models.Restaurant;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

@AllArgsConstructor
public class RestaurantInputDisassembler implements Function<Restaurant, RestaurantInput> {

  private final ModelMapper modelMapper;

  @Override
  public RestaurantInput apply(Restaurant t) {
    return modelMapper.map(t, RestaurantInput.class);
  }
}
