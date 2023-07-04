package km1.algafood.api.assemblers;

import java.util.Collection;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;
import km1.algafood.api.models.input.CityInput;
import km1.algafood.domain.models.City;
import km1.algafood.domain.models.State;

@Component
public class CityInputDisassembler {

  private final ModelMapper modelMapper;

  public CityInputDisassembler(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public City toDomainObject(CityInput source) {
    return modelMapper.map(source, City.class);
  }

  public Collection<City> toCollectionDomainObject(Collection<CityInput> sourceCollection) {
    return sourceCollection.stream()
        .map(source -> modelMapper.map(source, City.class))
        .collect(Collectors.toList());
  }

public void copyToDomainObject(City city, @Valid CityInput cityInput) {
    city.setState(new State());
    this.modelMapper.map(cityInput, city);
}
}
