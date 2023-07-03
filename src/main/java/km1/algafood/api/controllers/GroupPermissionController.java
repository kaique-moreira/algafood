package km1.algafood.api.controllers;

import java.util.List;
import km1.algafood.api.assemblers.PermissionModelAssembler;
import km1.algafood.api.models.PermissionModel;
import km1.algafood.domain.services.GroupRegisterService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/groups/{groupId}/permissions")
@AllArgsConstructor
public class GroupPermissionController {

  private final GroupRegisterService rRegisterService;
  private final PermissionModelAssembler pmModelAssembler;

  @GetMapping
  public List<PermissionModel> list(@PathVariable Long groupId) {
    return (List<PermissionModel>)
        pmModelAssembler.toCollectionModel(rRegisterService.fetchByID(groupId).getPermissions());
  }

  @PutMapping("/{permissionId}")
  public void associate(@PathVariable Long groupId, @PathVariable Long permissionId) {
    rRegisterService.associatePermission(groupId, permissionId);
  }

  @DeleteMapping("/{permissionId}")
  public void dsassociate(@PathVariable Long groupId, @PathVariable Long permissionId) {
    rRegisterService.desassociatePermission(groupId, permissionId);
  }
}
