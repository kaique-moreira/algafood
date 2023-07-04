package km1.algafood.api.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
import km1.algafood.domain.repositories.CuisineRepository;
import km1.algafood.domain.services.CuisineRegisterService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/cuisines")
@AllArgsConstructor
public class CuisineController {

  private final CuisineRegisterService registerService;
  private final CuisineInputDisassembler disassembler;
  private final CuisineModelAssembler assembler;
  private final CuisineRepository repository;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CuisineModel add(@RequestBody @Valid CuisineInput cuisineinput) {
    var toRegister = disassembler.toDomainObject(cuisineinput);
    var registered =  registerService.register(toRegister);
    var cuisineModel = assembler.toModel(registered);
    return cuisineModel;
  }

  @GetMapping
  public Page<CuisineModel> list(Pageable pageable) {
    var cuisinesPage = repository.findAll(pageable);
    var cuisinesModel = assembler.toCollectionModel(cuisinesPage.getContent());
    var cuisinesModelPage = new PageImpl<CuisineModel>(cuisinesModel);
    return cuisinesModelPage;
  }

  @GetMapping("/{id}")
  public CuisineModel fetch(@PathVariable Long id) {
    var cuisine = registerService.tryFetch(id);
    var cuisineModel = assembler.toModel(cuisine);
    return cuisineModel;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long cuisineId) {
    registerService.tryRemove(cuisineId);
  }

  @PutMapping("/{id}")
  public CuisineModel update(@PathVariable Long cuisineId, @RequestBody @Valid CuisineInput cuisineInput) {
    var cuisine = registerService.tryFetch(cuisineId);
    disassembler.copyToDomainObject(cuisine, cuisineInput);
    cuisine =  registerService.register(cuisine);
    var cuisineModel = assembler.toModel(cuisine);
    return cuisineModel;
  }
}
