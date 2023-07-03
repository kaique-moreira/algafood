package km1.algafood.api.assemblers;

import java.util.Collection;
import java.util.stream.Collectors;
import km1.algafood.api.models.StateModel;
import km1.algafood.domain.models.State;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class StateModelAssembler {

  private final ModelMapper modelMapper;

  public StateModelAssembler(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public StateModel toModel(State source) {
    return modelMapper.map(source, StateModel.class);
  }

  public Collection<StateModel> toCollectionModel(Collection<State> sourceCollection) {
    return sourceCollection.stream()
        .map(source -> modelMapper.map(source, StateModel.class))
        .collect(Collectors.toList());
  }
}



