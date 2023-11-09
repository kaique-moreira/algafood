package km1.algafood.api.assemblers;

import java.util.function.Function;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import km1.algafood.api.models.UserInput;
import km1.algafood.domain.models.User;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class UserInputDisassembler implements Function<UserInput, User> {

  private final ModelMapper modelMapper;

  @Override
  public User apply(UserInput cityInput) {
    return modelMapper.map(cityInput, User.class);
  }
}
