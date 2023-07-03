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
import km1.algafood.api.assemblers.CityModelAssembler;
import km1.algafood.api.assemblers.CityInputDisassembler;
import km1.algafood.api.models.CityModel;
import km1.algafood.api.models.CityInput;
import km1.algafood.domain.services.CityRegisterService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/cities")
@AllArgsConstructor
public class CityController {
  
  private final CityRegisterService registerService;
  private final CityInputDisassembler disassembler;
  private final CityModelAssembler assembler;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CityModel addCity(@RequestBody @Valid CityInput cityInput) {
    var toRegister = disassembler.toDomainObject(cityInput);
    var registered = registerService.register(toRegister);
    var cityModel = assembler.toModel(registered);
    return cityModel;
  }

  @GetMapping
  public List<CityModel> list() {
    var cities = registerService.fetchAll();
    var citiesModel = assembler.toCollectionModel(cities);
    return (List<CityModel>) citiesModel;
  }

  @GetMapping("/{id}")
  public CityModel fetch(@PathVariable Long id) {
    var city = registerService.fetchByID(id);
    var cityModel = assembler.toModel(city);
    return cityModel;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void removeCityById(@PathVariable Long id) {
    registerService.remove(id);
  }

  @PutMapping("/{id}")
  public CityModel update(@PathVariable Long id,@RequestBody @Valid CityInput cityInput) {
    var toUpdate = disassembler.toDomainObject(cityInput);
    var updatetd =  registerService.update(id, toUpdate);
    var cityModel = assembler.toModel(updatetd);
    return cityModel;
  }
}
