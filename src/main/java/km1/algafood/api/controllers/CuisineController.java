package km1.algafood.api.controllers;

import java.util.List;
import km1.algafood.api.assemblers.CuisineDtoAssembler;
import km1.algafood.api.assemblers.CuisineInputDisassembler;
import km1.algafood.api.models.CuisineDto;
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
  private final CuisineDtoAssembler assembler;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CuisineDto saveCuisine(@RequestBody CuisineInput cuisineinput) {
    var toRegister = disassembler.apply(cuisineinput);
    var registered =  registerService.register(toRegister);
    var cuisineDto = assembler.apply(registered);
    return cuisineDto;
  }

  @GetMapping
  public List<CuisineDto> findCuisines() {
    var cuisines = registerService.fetchAll();
    var cuisinesDto = cuisines.stream().map(assembler).toList();
    return cuisinesDto;
  }

  @GetMapping("/{id}")
  public CuisineDto findCuisineById(@PathVariable Long id) {
    var cuisine = registerService.fetchByID(id);
    var cuisineDto = assembler.apply(cuisine);
    return cuisineDto;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCuisineById(@PathVariable Long id) {
    registerService.remove(id);
  }

  @PutMapping("/{id}")
  public CuisineDto updateCuisineById(@PathVariable Long id, @RequestBody @Valid CuisineInput cuisineInput) {
    var toUpdate = disassembler.apply(cuisineInput);
    var updated = registerService.update(id, toUpdate);
    var cuisineDto = assembler.apply(updated);
    return cuisineDto;
  }
}
