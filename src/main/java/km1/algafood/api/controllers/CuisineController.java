package km1.algafood.api.controllers;

import java.util.List;
import km1.algafood.api.assemblers.CuisineModelAssembler;
import km1.algafood.api.assemblers.CuisineInputDisassembler;
import km1.algafood.api.models.CuisineModel;
import km1.algafood.api.models.CuisineInput;
import km1.algafood.domain.services.CuisineRegisterService;
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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/cuisines")
@AllArgsConstructor
public class CuisineController {

  private final CuisineRegisterService registerService;
  private final CuisineInputDisassembler disassembler;
  private final CuisineModelAssembler assembler;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CuisineModel saveCuisine(@RequestBody @Valid CuisineInput cuisineinput) {
    var toRegister = disassembler.apply(cuisineinput);
    var registered =  registerService.register(toRegister);
    var cuisineModel = assembler.apply(registered);
    return cuisineModel;
  }

  @GetMapping
  public List<CuisineModel> findCuisines() {
    var cuisines = registerService.fetchAll();
    var cuisinesModel = cuisines.stream().map(assembler).toList();
    return cuisinesModel;
  }

  @GetMapping("/{id}")
  public CuisineModel findCuisineById(@PathVariable Long id) {
    var cuisine = registerService.fetchByID(id);
    var cuisineModel = assembler.apply(cuisine);
    return cuisineModel;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCuisineById(@PathVariable Long id) {
    registerService.remove(id);
  }

  @PutMapping("/{id}")
  public CuisineModel updateCuisineById(@PathVariable Long id, @RequestBody @Valid CuisineInput cuisineInput) {
    var toUpdate = disassembler.apply(cuisineInput);
    var updated = registerService.update(id, toUpdate);
    var cuisineModel = assembler.apply(updated);
    return cuisineModel;
  }
}
