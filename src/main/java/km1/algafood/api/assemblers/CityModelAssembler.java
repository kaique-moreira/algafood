package km1.algafood.api.assemblers;

import java.util.Collection;
import java.util.stream.Collectors;
import km1.algafood.api.models.CityModel;
import km1.algafood.domain.models.City;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CityModelAssembler {

  private final ModelMapper modelMapper;

  public CityModelAssembler(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public CityModel toModel(City source) {
    return modelMapper.map(source, CityModel.class);
  }

  public Collection<CityModel> toCollectionModel(Collection<City> sourceCollection) {
    return sourceCollection.stream()
        .map(source -> modelMapper.map(source, CityModel.class))
        .collect(Collectors.toList());
  }
}



