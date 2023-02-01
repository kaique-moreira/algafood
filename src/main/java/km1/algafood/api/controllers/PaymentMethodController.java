package km1.algafood.api.controllers;

import java.util.List;
import km1.algafood.api.assemblers.PaymentMethodModelAssembler;
import km1.algafood.api.assemblers.PaymentMethodInputDisassembler;
import km1.algafood.api.models.PaymentMethodModel;
import km1.algafood.api.models.PaymentMethodInput;
import km1.algafood.domain.services.PaymentMethodRegisterService;
import lombok.AllArgsConstructor;
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

@RestController
@RequestMapping("/api/v1/payment-methods")
@AllArgsConstructor
public class PaymentMethodController {

  private final PaymentMethodRegisterService registerService;
  private final PaymentMethodInputDisassembler disassembler;
  private final PaymentMethodModelAssembler assembler;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public PaymentMethodModel savePaymentMethod(@RequestBody @Valid PaymentMethodInput cuisineinput) {
    var toRegister = disassembler.apply(cuisineinput);
    var registered =  registerService.register(toRegister);
    var cuisineModel = assembler.apply(registered);
    return cuisineModel;
  }

  @GetMapping
  public List<PaymentMethodModel> findPaymentMethods() {
    var cuisines = registerService.fetchAll();
    var cuisinesModel = cuisines.stream().map(assembler).toList();
    return cuisinesModel;
  }

  @GetMapping("/{id}")
  public PaymentMethodModel findPaymentMethodById(@PathVariable Long id) {
    var cuisine = registerService.fetchByID(id);
    var cuisineModel = assembler.apply(cuisine);
    return cuisineModel;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletePaymentMethodById(@PathVariable Long id) {
    registerService.remove(id);
  }

  @PutMapping("/{id}")
  public PaymentMethodModel updatePaymentMethodById(@PathVariable Long id, @RequestBody @Valid PaymentMethodInput cuisineInput) {
    var toUpdate = disassembler.apply(cuisineInput);
    var updated = registerService.update(id, toUpdate);
    var cuisineModel = assembler.apply(updated);
    return cuisineModel;
  }
}
