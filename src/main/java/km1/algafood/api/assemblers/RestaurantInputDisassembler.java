package km1.algafood.api.assemblers;

import java.util.function.Function;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import km1.algafood.api.models.RestaurantInput;
import km1.algafood.domain.models.Restaurant;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class RestaurantInputDisassembler implements Function<RestaurantInput, Restaurant> {

  private final ModelMapper modelMapper;

  @Override
  public Restaurant apply(RestaurantInput restaurantInput) {
    return modelMapper.map(restaurantInput, Restaurant.class);
  }
}
