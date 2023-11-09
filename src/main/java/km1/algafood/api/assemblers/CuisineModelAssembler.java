package km1.algafood.api.assemblers;

import java.util.function.Function;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import km1.algafood.api.models.CuisineModel;
import km1.algafood.domain.models.Cuisine;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CuisineModelAssembler implements Function<Cuisine, CuisineModel> {

  private final ModelMapper modelMapper;

  @Override
  public CuisineModel apply(Cuisine t) {
    return modelMapper.map(t, CuisineModel.class);
  }
}
