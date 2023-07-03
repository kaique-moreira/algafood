package km1.algafood.api.assemblers;

import java.util.Collection;
import java.util.stream.Collectors;
import km1.algafood.api.models.PermissionModel;
import km1.algafood.domain.models.Permission;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PermissionModelAssembler {

  private final ModelMapper modelMapper;

  public PermissionModelAssembler(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public PermissionModel toModel(Permission source) {
    return modelMapper.map(source, PermissionModel.class);
  }

  public Collection<PermissionModel> toCollectionModel(Collection<Permission> sourceCollection) {
    return sourceCollection.stream()
        .map(source -> modelMapper.map(source, PermissionModel.class))
        .collect(Collectors.toList());
  }
}


