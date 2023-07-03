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
import km1.algafood.api.assemblers.CuisineInputDisassembler;
import km1.algafood.api.assemblers.CuisineModelAssembler;
import km1.algafood.api.models.CuisineModel;
import km1.algafood.api.models.input.CuisineInput;
import km1.algafood.domain.services.CuisineRegisterService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/cuisines")
@AllArgsConstructor
public class CuisineController {

  private final CuisineRegisterService registerService;
  private final CuisineInputDisassembler disassembler;
  private final CuisineModelAssembler assembler;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CuisineModel add(@RequestBody @Valid CuisineInput cuisineinput) {
    var toRegister = disassembler.toDomainObject(cuisineinput);
    var registered =  registerService.register(toRegister);
    var cuisineModel = assembler.toModel(registered);
    return cuisineModel;
  }

  @GetMapping
  public List<CuisineModel> list() {
    var cuisines = registerService.fetchAll();
    var cuisinesModel = assembler.toCollectionModel(cuisines);
    return (List<CuisineModel>) cuisinesModel;
  }

  @GetMapping("/{id}")
  public CuisineModel fetch(@PathVariable Long id) {
    var cuisine = registerService.fetchByID(id);
    var cuisineModel = assembler.toModel(cuisine);
    return cuisineModel;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    registerService.remove(id);
  }

  @PutMapping("/{id}")
  public CuisineModel update(@PathVariable Long id, @RequestBody @Valid CuisineInput cuisineInput) {
    var toUpdate = disassembler.toDomainObject(cuisineInput);
    var updated = registerService.update(id, toUpdate);
    var cuisineModel = assembler.toModel(updated);
    return cuisineModel;
  }
}
