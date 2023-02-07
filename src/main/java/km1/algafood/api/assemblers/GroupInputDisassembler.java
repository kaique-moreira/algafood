package km1.algafood.api.assemblers;

import java.util.function.Function;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import km1.algafood.api.models.GroupInput;
import km1.algafood.domain.models.Group;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class GroupInputDisassembler implements Function<GroupInput, Group> {

  private final ModelMapper modelMapper;

  @Override
  public Group apply(GroupInput cityInput) {
    return modelMapper.map(cityInput, Group.class);
  }
}
