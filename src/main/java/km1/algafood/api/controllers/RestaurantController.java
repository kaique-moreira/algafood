package km1.algafood.api.controllers;

import jakarta.validation.Valid;
import java.util.List;
import km1.algafood.api.assemblers.RestaurantDtoAssembler;
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
  private final RestaurantDtoAssembler assembler;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public RestaurantModel saveRestaurant(@RequestBody @Valid RestaurantInput restaurantInput) {
    try {
    var toRegister = disassembler.apply(restaurantInput);
    var registered = registerService.register(toRegister);
    var restaurantDto = assembler.apply(registered);
    return restaurantDto;
      
    } catch (CuisineNotFountException | CityNotFountException e) {
      throw new DomainException(e.getMessage());
    }
  }

  @GetMapping
  public List<RestaurantModel> findCities() {
    var cities = registerService.fetchAll();
    var citiesDto = cities.stream().map(assembler).toList();
    return citiesDto;
  }

  @GetMapping("/{id}")
  public RestaurantModel findRestaurantById(@PathVariable Long id) {
    var restaurant = registerService.fetchByID(id);
    var restaurantDto = assembler.apply(restaurant);
    return restaurantDto;
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
      var restaurantDto = assembler.apply(updatetd);
      return restaurantDto;

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
