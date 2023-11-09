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

import km1.algafood.api.assemblers.RestaurantDtoAssembler;
import km1.algafood.api.assemblers.RestaurantInputDisassembler;
import km1.algafood.api.models.RestaurantDto;
import km1.algafood.api.models.RestaurantInput;
import km1.algafood.domain.services.RestaurantRegisterService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/restaurants")
@AllArgsConstructor
public class RestaurantController {
  private final RestaurantRegisterService registerService;
  private final RestaurantInputDisassembler disassembler;
  private final RestaurantDtoAssembler assembler;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public RestaurantDto saveRestaurant(@RequestBody RestaurantInput restaurantInput) {
    var toRegister = disassembler.apply(restaurantInput);
    var registered =  registerService.register(toRegister);
    var restaurantDto = assembler.apply(registered);
    return restaurantDto;
  }

  @GetMapping
  public List<RestaurantDto> findRestaurants() {
    var restaurants =  registerService.fetchAll();
    var restaurantsDto = restaurants.stream().map(assembler).toList();
    return restaurantsDto;
  }

  @GetMapping("/{id}")
  public RestaurantDto findRestaurantById(@PathVariable Long id) {
    var restaurant = registerService.fetchByID(id);
    var restaurantDto = assembler.apply(restaurant);
    return restaurantDto;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteRestaurantDtoById(@PathVariable Long id) {
    registerService.remove(id);
  }

  @PutMapping("/{id}")
  public RestaurantDto updateRestaurantById(@PathVariable Long id,@RequestBody RestaurantInput restaurantInput) {
    var toUpdate = disassembler.apply(restaurantInput);
    var updated =  registerService.update(id, toUpdate);
    var restaurantDto = assembler.apply(updated);
    return restaurantDto;
  }
}
