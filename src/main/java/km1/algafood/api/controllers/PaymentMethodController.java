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
import km1.algafood.api.assemblers.PaymentMethodInputDisassembler;
import km1.algafood.api.assemblers.PaymentMethodModelAssembler;
import km1.algafood.api.models.PaymentMethodModel;
import km1.algafood.api.models.input.PaymentMethodInput;
import km1.algafood.domain.services.PaymentMethodRegisterService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/payment-methods")
@AllArgsConstructor
public class PaymentMethodController {

  private final PaymentMethodRegisterService registerService;
  private final PaymentMethodInputDisassembler disassembler;
  private final PaymentMethodModelAssembler assembler;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public PaymentMethodModel add(@RequestBody @Valid PaymentMethodInput cuisineinput) {
    var toRegister = disassembler.toDomainObject(cuisineinput);
    var registered =  registerService.register(toRegister);
    var patmentMethodModel = assembler.toModel(registered);
    return patmentMethodModel;
  }

  @GetMapping
  public List<PaymentMethodModel> list() {
    var paymentMethods = registerService.fetchAll();
    var patmentMethodsModel = assembler.toCollectionModel(paymentMethods);
    return (List<PaymentMethodModel>) patmentMethodsModel;
  }

  @GetMapping("/{id}")
  public PaymentMethodModel fetch(@PathVariable Long id) {
    var cuisine = registerService.fetchByID(id);
    var patmentMethodModel = assembler.toModel(cuisine);
    return patmentMethodModel;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    registerService.remove(id);
  }

  @PutMapping("/{id}")
  public PaymentMethodModel update(@PathVariable Long id, @RequestBody @Valid PaymentMethodInput cuisineInput) {
    var toUpdate = disassembler.toDomainObject(cuisineInput);
    var updated = registerService.update(id, toUpdate);
    var patmentMethodModel = assembler.toModel(updated);
    return patmentMethodModel;
  }
}
