package km1.algafood.api.assemblers;

import java.util.function.Function;
import km1.algafood.api.models.StateInput;
import km1.algafood.domain.models.State;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

@AllArgsConstructor
public class StateInputDisassembler implements Function<State, StateInput> {

  private final ModelMapper modelMapper;

  @Override
  public StateInput apply(State t) {
    return modelMapper.map(t, StateInput.class);
  }
}
