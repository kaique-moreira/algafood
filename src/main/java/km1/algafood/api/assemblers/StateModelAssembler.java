package km1.algafood.api.assemblers;

import java.util.function.Function;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import km1.algafood.api.models.StateModel;
import km1.algafood.domain.models.State;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class StateModelAssembler implements Function<State, StateModel> {

  private final ModelMapper modelMapper;

  @Override
  public StateModel apply(State t) {
    return modelMapper.map(t, StateModel.class);
  }
}
