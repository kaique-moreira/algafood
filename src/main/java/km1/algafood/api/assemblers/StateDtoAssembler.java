package km1.algafood.api.assemblers;

import java.util.function.Function;
import km1.algafood.api.models.StateDto;
import km1.algafood.domain.models.State;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

@AllArgsConstructor
public class StateDtoAssembler implements Function<State, StateDto> {

  private final ModelMapper modelMapper;

  @Override
  public StateDto apply(State t) {
    return modelMapper.map(t, StateDto.class);
  }
}
