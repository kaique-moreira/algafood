package km1.algafood.api.assemblers;

import java.util.function.Function;
import km1.algafood.api.models.GroupModel;
import km1.algafood.domain.models.Group;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GroupModelAssembler implements Function<Group, GroupModel> {

  private final ModelMapper modelMapper;

  @Override
  public GroupModel apply(Group t) {
    return modelMapper.map(t, GroupModel.class);
  }
}
