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

import km1.algafood.domain.models.State;
import km1.algafood.domain.services.StateRegisterService;

@RestController
@RequestMapping("/api/v1/states")
public class StateController {
  
  private final StateRegisterService registerService;

  public StateController(StateRegisterService registerService) {
    this.registerService = registerService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public State saveState(@RequestBody State state) {
    return registerService.register(state);
  }

  @GetMapping
  public List<State> findStates() {
    return registerService.fetchAll();
  }

  @GetMapping("/{id}")
  public State findStateById(@PathVariable Long id) {
    return registerService.fetchByID(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteStateById(@PathVariable Long id) {
    registerService.remove(id);
  }

  @PutMapping("/{id}")
  public State updateStateById(@PathVariable Long id,@RequestBody State state) {
    return registerService.update(id, state);
  }


}
