package km1.algafood.api.assemblers;

import java.util.Collection;
import java.util.stream.Collectors;
import km1.algafood.api.models.UserModel;
import km1.algafood.domain.models.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserModelAssembler {

  private final ModelMapper modelMapper;

  public UserModelAssembler(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public UserModel toModel(User source) {
    return modelMapper.map(source, UserModel.class);
  }

  public Collection<UserModel> toCollectionModel(Collection<User> sourceCollection) {
    return sourceCollection.stream()
        .map(source -> modelMapper.map(source, UserModel.class))
        .collect(Collectors.toList());
  }
}

