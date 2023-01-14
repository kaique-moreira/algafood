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

import km1.algafood.domain.models.City;
import km1.algafood.domain.services.CityRegisterService;

@RestController
@RequestMapping("/api/v1/cities")
public class CityController {
  
  private final CityRegisterService registerService;

  public CityController(CityRegisterService registerService) {
    this.registerService = registerService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public City saveCity(@RequestBody City city) {
    return registerService.register(city);
  }

  @GetMapping
  public List<City> findCitys() {
    return registerService.fetchAll();
  }

  @GetMapping("/{id}")
  public City findCityById(@PathVariable Long id) {
    return registerService.fetchByID(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCityById(@PathVariable Long id) {
    registerService.remove(id);
  }

  @PutMapping("/{id}")
  public City updateCityById(@PathVariable Long id,@RequestBody City city) {
    return registerService.update(id, city);
  }


}
