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
import km1.algafood.api.assemblers.UserInputDisassembler;
import km1.algafood.api.assemblers.UserModelAssembler;
import km1.algafood.api.models.UserModel;
import km1.algafood.api.models.input.PasswordInput;
import km1.algafood.api.models.input.UserInput;
import km1.algafood.domain.services.UserRegisterService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
  
  private final UserRegisterService registerService;
  private final UserInputDisassembler disassembler;
  private final UserModelAssembler assembler;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserModel add(@RequestBody @Valid UserInput userInput) {
    var toRegister = disassembler.toDomainObject(userInput);
    var registered = registerService.register(toRegister);
    var userModel = assembler.toModel(registered);
    return userModel;
  }

  @GetMapping
  public List<UserModel> list() {
    var users = registerService.fetchAll();
    var usersModel = assembler.toCollectionModel(users);
    return (List<UserModel>) usersModel;
  }

  @GetMapping("/{id}")
  public UserModel fetch(@PathVariable Long id) {
    var user = registerService.fetchByID(id);
    var userModel = assembler.toModel(user);
    return userModel;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    registerService.remove(id);
  }

  @PutMapping("/{id}")
  public UserModel update(@PathVariable Long id,@RequestBody @Valid UserInput userInput) {
    var toUpdate = disassembler.toDomainObject(userInput);
    var updatetd =  registerService.update(id, toUpdate);
    var userModel = assembler.toModel(updatetd);
    return userModel;
  }

  @PutMapping("/{id}/password")
  public void updateUserPasswordById(@PathVariable Long id,@RequestBody @Valid PasswordInput passwordInput) {
    registerService.updatePassword(id, passwordInput.getCurrentPassword(), passwordInput.getNewPassword());
  }


}
