package km1.algafood.api.controllers;

import jakarta.validation.Valid;
import java.util.List;
import km1.algafood.api.assemblers.RestaurantModelAssembler;
import km1.algafood.api.assemblers.RestaurantInputDisassembler;
import km1.algafood.api.models.RestaurantInput;
import km1.algafood.api.models.RestaurantModel;
import km1.algafood.domain.exceptions.CityNotFountException;
import km1.algafood.domain.exceptions.CuisineNotFountException;
import km1.algafood.domain.exceptions.DomainException;
import km1.algafood.domain.services.RestaurantRegisterService;
import lombok.AllArgsConstructor;
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

@RestController
@RequestMapping("/api/v1/restaurants")
@AllArgsConstructor
public class RestaurantController {

  private final RestaurantRegisterService registerService;
  private final RestaurantInputDisassembler disassembler;
  private final RestaurantModelAssembler assembler;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public RestaurantModel saveRestaurant(@RequestBody @Valid RestaurantInput restaurantInput) {
    try {
    var toRegister = disassembler.apply(restaurantInput);
    var registered = registerService.register(toRegister);
    var restaurantModel = assembler.apply(registered);
    return restaurantModel;
      
    } catch (CuisineNotFountException | CityNotFountException e) {
      throw new DomainException(e.getMessage());
    }
  }

  @GetMapping
  public List<RestaurantModel> findCities() {
    var cities = registerService.fetchAll();
    var citiesModel = cities.stream().map(assembler).toList();
    return citiesModel;
  }

  @GetMapping("/{id}")
  public RestaurantModel findRestaurantById(@PathVariable Long id) {
    var restaurant = registerService.fetchByID(id);
    var restaurantModel = assembler.apply(restaurant);
    return restaurantModel;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteRestaurantById(@PathVariable Long id) {
    registerService.remove(id);
  }

  @PutMapping("/{id}")
  public RestaurantModel updateRestaurantById(
      @PathVariable Long id, @RequestBody @Valid RestaurantInput restaurantInput) {
    try {
      var toUpdate = disassembler.apply(restaurantInput);
      var updatetd = registerService.update(id, toUpdate);
      var restaurantModel = assembler.apply(updatetd);
      return restaurantModel;

    } catch (CuisineNotFountException | CityNotFountException  e) {
      throw new DomainException(e.getMessage());
    }
  }

  @PutMapping("/{id}/active")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void active(@PathVariable Long id) {
    registerService.active(id);
  }

  @DeleteMapping("/{id}/active")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void disactive(@PathVariable Long id) {
    registerService.disactive(id);
  }
}
