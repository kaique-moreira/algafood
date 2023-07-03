package km1.algafood.api.assemblers;

import java.util.Collection;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import km1.algafood.api.models.input.RestaurantInput;
import km1.algafood.domain.models.Restaurant;

@Component
public class RestaurantInputDisassembler {

  private final ModelMapper modelMapper;

  public RestaurantInputDisassembler(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public Restaurant toDomainObject(RestaurantInput source) {
    return modelMapper.map(source, Restaurant.class);
  }

  public Collection<Restaurant> toCollectionDomainObject(Collection<RestaurantInput> sourceCollection) {
    return sourceCollection.stream()
        .map(source -> modelMapper.map(source, Restaurant.class))
        .collect(Collectors.toList());
  }
}

