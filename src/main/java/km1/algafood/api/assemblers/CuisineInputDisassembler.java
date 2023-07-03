package km1.algafood.api.assemblers;

import java.util.Collection;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import km1.algafood.api.models.input.CuisineInput;
import km1.algafood.domain.models.Cuisine;

@Component
public class CuisineInputDisassembler {

  private final ModelMapper modelMapper;

  public CuisineInputDisassembler(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public Cuisine toDomainObject(CuisineInput source) {
    return modelMapper.map(source, Cuisine.class);
  }

  public Collection<Cuisine> toCollectionDomainObject(Collection<CuisineInput> sourceCollection) {
    return sourceCollection.stream()
        .map(source -> modelMapper.map(source, Cuisine.class))
        .collect(Collectors.toList());
  }
}

