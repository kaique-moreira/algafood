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

import km1.algafood.api.assemblers.StateDtoAssembler;
import km1.algafood.api.assemblers.StateInputDisassembler;
import km1.algafood.api.models.StateDto;
import km1.algafood.api.models.StateInput;
import km1.algafood.domain.services.StateRegisterService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/states")
@AllArgsConstructor
public class StateController {
  
  private final StateRegisterService registerService;
  private final StateInputDisassembler disassembler;
  private final StateDtoAssembler assembler;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public StateDto saveState(@RequestBody StateInput stateInput) {
    var toRegister = disassembler.apply(stateInput);
    var registered = registerService.register(toRegister);
    var stateDto = assembler.apply(registered);
    return stateDto;
  }

  @GetMapping
  public List<StateDto> findStates() {
    var states =  registerService.fetchAll();
    var statesDto = states.stream().map(assembler).toList();
    return statesDto;
  }

  @GetMapping("/{id}")
  public StateDto findStateById(@PathVariable Long id) {
    var state =  registerService.fetchByID(id);
    var stateDto = assembler.apply(state);
    return stateDto;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteStateDtoById(@PathVariable Long id) {
    registerService.remove(id);
  }

  @PutMapping("/{id}")
  public StateDto updateStateById(@PathVariable Long id,@RequestBody StateInput stateInput) {
    var toUpdate = disassembler.apply(stateInput);
    var updated =  registerService.update(id, toUpdate);
    var stateDto = assembler.apply(updated);
    return stateDto;
  }
}
