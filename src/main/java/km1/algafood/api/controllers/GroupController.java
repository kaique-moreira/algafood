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
import km1.algafood.api.assemblers.GroupInputDisassembler;
import km1.algafood.api.assemblers.GroupModelAssembler;
import km1.algafood.api.models.GroupModel;
import km1.algafood.api.models.input.GroupInput;
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
  public GroupModel add(@RequestBody @Valid GroupInput groupInput) {
    var toRegister = disassembler.toDomainObject(groupInput);
    var registered = registerService.register(toRegister);
    var groupModel = assembler.toModel(registered);
    return groupModel;
  }

  @GetMapping
  public List<GroupModel> list() {
    var groups = registerService.fetchAll();
    var groupsModel = assembler.toCollectionModel(groups);
    return (List<GroupModel>)groupsModel;
  }

  @GetMapping("/{id}")
  public GroupModel fetch(@PathVariable Long id) {
    var group = registerService.fetchByID(id);
    var groupModel = assembler.toModel(group);
    return groupModel;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    registerService.remove(id);
  }

  @PutMapping("/{id}")
  public GroupModel update(@PathVariable Long id,@RequestBody @Valid GroupInput groupInput) {
    var toUpdate = disassembler.toDomainObject(groupInput);
    var updatetd =  registerService.update(id, toUpdate);
    var groupModel = assembler.toModel(updatetd);
    return groupModel;
  }


}
