package km1.algafood.api.assemblers;

import java.util.function.Function;
import km1.algafood.api.models.UserModel;
import km1.algafood.domain.models.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserModelAssembler implements Function<User, UserModel> {

  private final ModelMapper modelMapper;

  @Override
  public UserModel apply(User t) {
    return modelMapper.map(t, UserModel.class);
  }
}
