package km1.algafood.api.controllers;

import java.util.List;
import km1.algafood.api.assemblers.GroupModelAssembler;
import km1.algafood.api.models.GroupModel;
import km1.algafood.domain.services.UserRegisterService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/{userId}/groups")
@AllArgsConstructor
public class UserGroupController {

  private final UserRegisterService userRegisterService;
  private final GroupModelAssembler groupModelAssembler;

  @GetMapping
  public List<GroupModel> list(@PathVariable Long userId) {
    return (List<GroupModel>)
        groupModelAssembler.toCollectionModel(userRegisterService.fetchByID(userId).getGroups());
  }

  @PutMapping("/{groupId}")
  public void associate(@PathVariable Long userId, @PathVariable Long groupId) {
    userRegisterService.associateGroup(userId, groupId);
  }

  @DeleteMapping("/{groupId}")
  public void dsassociate(@PathVariable Long userId, @PathVariable Long groupId) {
    userRegisterService.desassociateGroup(userId, groupId);
  }
}
