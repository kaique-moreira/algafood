package km1.algafood.api.assemblers;

import java.util.function.Function;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import km1.algafood.api.models.RestaurantModel;
import km1.algafood.domain.models.Restaurant;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class RestaurantModelAssembler implements Function<Restaurant, RestaurantModel> {

  private final ModelMapper modelMapper;

  @Override
  public RestaurantModel apply(Restaurant t) {
    return modelMapper.map(t, RestaurantModel.class);
  }
}
