package km1.algafood.api.assemblers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import km1.algafood.api.models.CuisineModel;
import km1.algafood.domain.models.Cuisine;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CuisineModelAssembler {

  private final ModelMapper modelMapper;

  public CuisineModelAssembler(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public CuisineModel toModel(Cuisine source) {
    return modelMapper.map(source, CuisineModel.class);
  }

  public List<CuisineModel> toCollectionModel(Collection<Cuisine> sourceCollection) {
    return sourceCollection.stream()
        .map(source -> modelMapper.map(source, CuisineModel.class))
        .collect(Collectors.toList());
  }
}

