package km1.algafood.api.assemblers;

import java.util.function.Function;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import km1.algafood.api.models.CuisineDto;
import km1.algafood.domain.models.Cuisine;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CuisineDtoAssembler implements Function<Cuisine, CuisineDto> {

  private final ModelMapper modelMapper;

  @Override
  public CuisineDto apply(Cuisine t) {
    return modelMapper.map(t, CuisineDto.class);
  }
}
