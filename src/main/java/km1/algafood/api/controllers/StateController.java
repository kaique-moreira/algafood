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
import km1.algafood.api.assemblers.StateInputDisassembler;
import km1.algafood.api.assemblers.StateModelAssembler;
import km1.algafood.api.models.StateInput;
import km1.algafood.api.models.StateModel;
import km1.algafood.domain.services.StateRegisterService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/states")
@AllArgsConstructor
public class StateController {
  
  private final StateRegisterService registerService;
  private final StateInputDisassembler disassembler;
  private final StateModelAssembler assembler;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public StateModel add(@RequestBody @Valid StateInput stateInput) {
    var toRegister = disassembler.toDomainObject(stateInput);
    var registered = registerService.register(toRegister);
    var stateModel = assembler.toModel(registered);
    return stateModel;
  }

  @GetMapping
  public List<StateModel> list() {
    var states =  registerService.fetchAll();
    var statesModel = assembler.toCollectionModel(states);
    return (List<StateModel>)statesModel;
  }

  @GetMapping("/{id}")
  public StateModel fetch(@PathVariable Long id) {
    var state =  registerService.fetchByID(id);
    var stateModel = assembler.toModel(state);
    return stateModel;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    registerService.remove(id);
  }

  @PutMapping("/{id}")
  public StateModel update(@PathVariable Long id,@RequestBody @Valid StateInput stateInput) {
    var toUpdate = disassembler.toDomainObject(stateInput);
    var updated =  registerService.update(id, toUpdate);
    var stateModel = assembler.toModel(updated);
    return stateModel;
  }
}
