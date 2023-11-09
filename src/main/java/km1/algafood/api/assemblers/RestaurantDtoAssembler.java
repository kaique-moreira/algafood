package km1.algafood.api.assemblers;

import java.util.function.Function;
import km1.algafood.api.models.RestaurantDto;
import km1.algafood.domain.models.Restaurant;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

@AllArgsConstructor
public class RestaurantDtoAssembler implements Function<Restaurant, RestaurantDto> {

  private final ModelMapper modelMapper;

  @Override
  public RestaurantDto apply(Restaurant t) {
    return modelMapper.map(t, RestaurantDto.class);
  }
}
