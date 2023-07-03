package km1.algafood.api.assemblers;

import java.util.Collection;
import java.util.stream.Collectors;
import km1.algafood.api.models.GroupInput;
import km1.algafood.domain.models.Group;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class GroupInputDisassembler {

  private final ModelMapper modelMapper;

  public GroupInputDisassembler(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public Group toDomainObject(GroupInput source) {
    return modelMapper.map(source, Group.class);
  }

  public Collection<Group> toCollectionDomainObject(Collection<GroupInput> sourceCollection) {
    return sourceCollection.stream()
        .map(source -> modelMapper.map(source, Group.class))
        .collect(Collectors.toList());
  }
}


