package km1.algafood.api.assemblers;

import java.util.function.Function;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import km1.algafood.api.models.CuisineInput;
import km1.algafood.domain.models.Cuisine;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CuisineInputDisassembler implements Function<CuisineInput, Cuisine> {

  private final ModelMapper modelMapper;

  @Override
  public Cuisine apply(CuisineInput cuisineInput) {
    return modelMapper.map(cuisineInput, Cuisine.class);
  }
}
