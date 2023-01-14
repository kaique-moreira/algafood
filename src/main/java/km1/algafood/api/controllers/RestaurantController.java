package km1.algafood.api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import km1.algafood.domain.models.Restaurant;
import km1.algafood.domain.services.RestaurantRegisterService;

@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {
  private final RestaurantRegisterService registerService;

  public RestaurantController(RestaurantRegisterService registerService) {
    this.registerService = registerService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Restaurant saveRestaurant(@RequestBody Restaurant restaurant) {
    return registerService.register(restaurant);
  }

  @GetMapping
  public List<Restaurant> findRestaurants() {
    return registerService.fetchAll();
  }

  @GetMapping("/{id}")
  public Restaurant findRestaurantById(@PathVariable Long id) {
    return registerService.fetchByID(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteRestaurantById(@PathVariable Long id) {
    registerService.remove(id);
  }

  @PutMapping("/{id}")
  public Restaurant updateRestaurantById(@PathVariable Long id,@RequestBody Restaurant restaurant) {
    return registerService.update(id, restaurant);
  }
}
