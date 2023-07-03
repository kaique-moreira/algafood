package km1.algafood.api.assemblers;

import java.util.Collection;
import java.util.stream.Collectors;
import km1.algafood.api.models.CityInput;
import km1.algafood.domain.models.City;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

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
}
