package km1.algafood.api.assemblers;

import java.util.function.Function;
import km1.algafood.api.models.RestaurantInput;
import km1.algafood.domain.models.Restaurant;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

@AllArgsConstructor
public class RestaurantInputDisassembler implements Function<RestaurantInput, Restaurant> {

  private final ModelMapper modelMapper;

  @Override
  public Restaurant apply(RestaurantInput restaurantInput) {
    return modelMapper.map(restaurantInput, Restaurant.class);
  }
}
