package km1.algafood.api.assemblers;

import java.util.function.Function;
import km1.algafood.api.models.StateInput;
import km1.algafood.domain.models.State;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StateInputDisassembler implements Function<StateInput, State> {

  private final ModelMapper modelMapper;

  @Override
  public State apply(StateInput stateInput) {
    return modelMapper.map(stateInput, State.class);
  }
}
