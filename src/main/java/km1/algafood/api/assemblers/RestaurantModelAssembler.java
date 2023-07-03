package km1.algafood.api.assemblers;

import java.util.Collection;
import java.util.stream.Collectors;
import km1.algafood.api.models.RestaurantModel;
import km1.algafood.domain.models.Restaurant;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RestaurantModelAssembler {

  private final ModelMapper modelMapper;

  public RestaurantModelAssembler(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public RestaurantModel toModel(Restaurant source) {
    return modelMapper.map(source, RestaurantModel.class);
  }

  public Collection<RestaurantModel> toCollectionModel(Collection<Restaurant> sourceCollection) {
    return sourceCollection.stream()
        .map(source -> modelMapper.map(source, RestaurantModel.class))
        .collect(Collectors.toList());
  }
}



