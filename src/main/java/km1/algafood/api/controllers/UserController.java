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
import km1.algafood.api.assemblers.UserModelAssembler;
import km1.algafood.api.assemblers.UserInputDisassembler;
import km1.algafood.api.models.UserModel;
import km1.algafood.api.models.PasswordInput;
import km1.algafood.api.models.UserInput;
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
  public UserModel saveUser(@RequestBody @Valid UserInput userInput) {
    var toRegister = disassembler.apply(userInput);
    var registered = registerService.register(toRegister);
    var userModel = assembler.apply(registered);
    return userModel;
  }

  @GetMapping
  public List<UserModel> findCities() {
    var cities = registerService.fetchAll();
    var citiesModel = cities.stream().map(assembler).toList();
    return citiesModel;
  }

  @GetMapping("/{id}")
  public UserModel findUserById(@PathVariable Long id) {
    var user = registerService.fetchByID(id);
    var userModel = assembler.apply(user);
    return userModel;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUserById(@PathVariable Long id) {
    registerService.remove(id);
  }

  @PutMapping("/{id}")
  public UserModel updateUserById(@PathVariable Long id,@RequestBody @Valid UserInput userInput) {
    var toUpdate = disassembler.apply(userInput);
    var updatetd =  registerService.update(id, toUpdate);
    var userModel = assembler.apply(updatetd);
    return userModel;
  }

  @PutMapping("/{id}/password")
  public void updateUserById(@PathVariable Long id,@RequestBody @Valid PasswordInput passwordInput) {
    registerService.updatePassword(id, passwordInput.getCurrentPassword(), passwordInput.getNewPassword());
  }


}
