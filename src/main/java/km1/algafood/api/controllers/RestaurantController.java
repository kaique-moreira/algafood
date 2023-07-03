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

import jakarta.validation.Valid;
import km1.algafood.api.assemblers.RestaurantInputDisassembler;
import km1.algafood.api.assemblers.RestaurantModelAssembler;
import km1.algafood.api.models.RestaurantModel;
import km1.algafood.api.models.input.RestaurantInput;
import km1.algafood.domain.services.RestaurantRegisterService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/restaurants")
@AllArgsConstructor
public class RestaurantController {

  private final RestaurantRegisterService registerService;
  private final RestaurantInputDisassembler disassembler;
  private final RestaurantModelAssembler assembler;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public RestaurantModel add(@RequestBody @Valid RestaurantInput restaurantInput) {
    var toRegister = disassembler.toDomainObject(restaurantInput);
    var registered = registerService.register(toRegister);
    var restaurantModel = assembler.toModel(registered);
    return restaurantModel;
  }

  @GetMapping
  public List<RestaurantModel> list() {
    var cities = registerService.fetchAll();
    var citiesModel = assembler.toCollectionModel(cities);
    return (List<RestaurantModel>) citiesModel;
  }

  @GetMapping("/{id}")
  public RestaurantModel fetch(@PathVariable Long id) {
    var restaurant = registerService.fetchByID(id);
    var restaurantModel = assembler.toModel(restaurant);
    return restaurantModel;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteRestaurantById(@PathVariable Long id) {
    registerService.remove(id);
  }

  @PutMapping("/{id}")
  public RestaurantModel update(
      @PathVariable Long id, @RequestBody @Valid RestaurantInput restaurantInput) {
    var toUpdate = disassembler.toDomainObject(restaurantInput);
    var updatetd = registerService.update(id, toUpdate);
    var restaurantModel = assembler.toModel(updatetd);
    return restaurantModel;
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

  @PutMapping("/active")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void active(@RequestBody List<Long> id) {
    registerService.active(id);
  }

  @DeleteMapping("/activations")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void disactive(@RequestBody List<Long> id) {
    registerService.disactive(id);
  }

  @PutMapping("/{id}/activations")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void open(@PathVariable Long id) {
    registerService.opening(id);
  }

  @PutMapping("/{id}/closure")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void closure(@PathVariable Long id) {
    registerService.closure(id);
  }
}
