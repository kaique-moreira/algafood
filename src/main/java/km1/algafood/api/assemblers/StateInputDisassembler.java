package km1.algafood.api.assemblers;

import java.util.Collection;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import km1.algafood.api.models.input.StateInput;
import km1.algafood.domain.models.State;

@Component
public class StateInputDisassembler {

  private final ModelMapper modelMapper;

  public StateInputDisassembler(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public State toDomainObject(StateInput source) {
    return modelMapper.map(source, State.class);
  }

  public Collection<State> toCollectionDomainObject(Collection<StateInput> sourceCollection) {
    return sourceCollection.stream()
        .map(source -> modelMapper.map(source, State.class))
        .collect(Collectors.toList());
  }
}

