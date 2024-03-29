package km1.algafood.api.assemblers;

import java.util.Collection;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;
import km1.algafood.api.models.input.UserInput;
import km1.algafood.domain.models.User;

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

public void copyToDomainObject(User user, @Valid UserInput userInput) {
    modelMapper.map(userInput, user);
}
}


