package km1.algafood.api.controllers;

import java.util.List;
import km1.algafood.api.assemblers.PaymentMethodDtoAssembler;
import km1.algafood.api.assemblers.PaymentMethodInputDisassembler;
import km1.algafood.api.models.PaymentMethodDto;
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
  private final PaymentMethodDtoAssembler assembler;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public PaymentMethodDto savePaymentMethod(@RequestBody @Valid PaymentMethodInput cuisineinput) {
    var toRegister = disassembler.apply(cuisineinput);
    var registered =  registerService.register(toRegister);
    var cuisineDto = assembler.apply(registered);
    return cuisineDto;
  }

  @GetMapping
  public List<PaymentMethodDto> findPaymentMethods() {
    var cuisines = registerService.fetchAll();
    var cuisinesDto = cuisines.stream().map(assembler).toList();
    return cuisinesDto;
  }

  @GetMapping("/{id}")
  public PaymentMethodDto findPaymentMethodById(@PathVariable Long id) {
    var cuisine = registerService.fetchByID(id);
    var cuisineDto = assembler.apply(cuisine);
    return cuisineDto;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletePaymentMethodById(@PathVariable Long id) {
    registerService.remove(id);
  }

  @PutMapping("/{id}")
  public PaymentMethodDto updatePaymentMethodById(@PathVariable Long id, @RequestBody @Valid PaymentMethodInput cuisineInput) {
    var toUpdate = disassembler.apply(cuisineInput);
    var updated = registerService.update(id, toUpdate);
    var cuisineDto = assembler.apply(updated);
    return cuisineDto;
  }
}
