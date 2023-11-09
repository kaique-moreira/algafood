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

import km1.algafood.domain.models.Cuisine;
import km1.algafood.domain.services.CuisineRegisterService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/cuisines")
@AllArgsConstructor
public class CuisineController {
  private final CuisineRegisterService registerService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Cuisine saveCuisine(@RequestBody Cuisine cuisine) {
    return registerService.register(cuisine);
  }

  @GetMapping
  public List<Cuisine> findCuisines() {
    return registerService.fetchAll();
  }

  @GetMapping("/{id}")
  public Cuisine findCuisineById(@PathVariable Long id) {
    return registerService.fetchByID(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCuisineById(@PathVariable Long id) {
    registerService.remove(id);
  }

  @PutMapping("/{id}")
  public Cuisine updateCuisineById(@PathVariable Long id,@RequestBody Cuisine cuisine) {
    return registerService.update(id, cuisine);
  }


}
