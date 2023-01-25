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
import km1.algafood.api.assemblers.CityDtoAssembler;
import km1.algafood.api.assemblers.CityInputDisassembler;
import km1.algafood.api.models.CityDto;
import km1.algafood.api.models.CityInput;
import km1.algafood.domain.services.CityRegisterService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/cities")
@AllArgsConstructor
public class CityController {
  
  private final CityRegisterService registerService;
  private final CityInputDisassembler disassembler;
  private final CityDtoAssembler assembler;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CityDto saveCity(@RequestBody @Valid CityInput cityInput) {
    var toRegister = disassembler.apply(cityInput);
    var registered = registerService.register(toRegister);
    var cityDto = assembler.apply(registered);
    return cityDto;
  }

  @GetMapping
  public List<CityDto> findCitys() {
    var cities = registerService.fetchAll();
    var citiesDto = cities.stream().map(assembler).toList();
    return citiesDto;
  }

  @GetMapping("/{id}")
  public CityDto findCityById(@PathVariable Long id) {
    var city = registerService.fetchByID(id);
    var cityDto = assembler.apply(city);
    return cityDto;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCityById(@PathVariable Long id) {
    registerService.remove(id);
  }

  @PutMapping("/{id}")
  public CityDto updateCityById(@PathVariable Long id,@RequestBody @Valid CityInput cityInput) {
    var toUpdate = disassembler.apply(cityInput);
    var updatetd =  registerService.update(id, toUpdate);
    var cityDto = assembler.apply(updatetd);
    return cityDto;
  }


}
