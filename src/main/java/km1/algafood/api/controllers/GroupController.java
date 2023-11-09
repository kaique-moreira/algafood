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
import km1.algafood.api.assemblers.GroupModelAssembler;
import km1.algafood.api.assemblers.GroupInputDisassembler;
import km1.algafood.api.models.GroupModel;
import km1.algafood.api.models.GroupInput;
import km1.algafood.domain.services.GroupRegisterService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/groups")
@AllArgsConstructor
public class GroupController {
  
  private final GroupRegisterService registerService;
  private final GroupInputDisassembler disassembler;
  private final GroupModelAssembler assembler;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public GroupModel saveGroup(@RequestBody @Valid GroupInput cityInput) {
    var toRegister = disassembler.apply(cityInput);
    var registered = registerService.register(toRegister);
    var cityModel = assembler.apply(registered);
    return cityModel;
  }

  @GetMapping
  public List<GroupModel> findCities() {
    var cities = registerService.fetchAll();
    var citiesModel = cities.stream().map(assembler).toList();
    return citiesModel;
  }

  @GetMapping("/{id}")
  public GroupModel findGroupById(@PathVariable Long id) {
    var city = registerService.fetchByID(id);
    var cityModel = assembler.apply(city);
    return cityModel;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteGroupById(@PathVariable Long id) {
    registerService.remove(id);
  }

  @PutMapping("/{id}")
  public GroupModel updateGroupById(@PathVariable Long id,@RequestBody @Valid GroupInput cityInput) {
    var toUpdate = disassembler.apply(cityInput);
    var updatetd =  registerService.update(id, toUpdate);
    var cityModel = assembler.apply(updatetd);
    return cityModel;
  }


}
