package km1.algafood.api.assemblers;

import java.util.Collection;
import java.util.stream.Collectors;
import km1.algafood.api.models.GroupModel;
import km1.algafood.domain.models.Group;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class GroupModelAssembler {

  private final ModelMapper modelMapper;

  public GroupModelAssembler(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public GroupModel toModel(Group source) {
    return modelMapper.map(source, GroupModel.class);
  }

  public Collection<GroupModel> toCollectionModel(Collection<Group> sourceCollection) {
    return sourceCollection.stream()
        .map(source -> modelMapper.map(source, GroupModel.class))
        .collect(Collectors.toList());
  }
}

