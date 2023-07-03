package km1.algafood.api.controllers;

import java.util.List;
import km1.algafood.api.assemblers.UserModelAssembler;
import km1.algafood.api.models.UserModel;
import km1.algafood.domain.services.RestaurantRegisterService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/restaurants/{restaurantId}/members")
@AllArgsConstructor
public class RestaurantUserController {

  private final RestaurantRegisterService restaurantRegisterService;
  private final UserModelAssembler userModelAssembler;

  @GetMapping
  public List<UserModel> list(@PathVariable Long restaurantId) {
    return (List<UserModel>)
        userModelAssembler.toCollectionModel(restaurantRegisterService.fetchByID(restaurantId).getMembers());
  }

  @PutMapping("/{userId}")
  public void associate(@PathVariable Long restaurantId, @PathVariable Long userId) {
    restaurantRegisterService.associateUser(restaurantId, userId);
  }

  @DeleteMapping("/{userId}")
  public void dsassociate(@PathVariable Long restaurantId, @PathVariable Long userId) {
    restaurantRegisterService.desassociateUser(restaurantId, userId);
  }
}
