package km1.algafood.api.assemblers;

import java.util.Collection;
import java.util.stream.Collectors;
import km1.algafood.api.models.UserInput;
import km1.algafood.domain.models.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserInputDisassembler {

  private final ModelMapper modelMapper;

  public UserInputDisassembler(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public User toDomainObject(UserInput source) {
    return modelMapper.map(source, User.class);
  }

  public Collection<User> toCollectionDomainObject(Collection<UserInput> sourceCollection) {
    return sourceCollection.stream()
        .map(source -> modelMapper.map(source, User.class))
        .collect(Collectors.toList());
  }
}


