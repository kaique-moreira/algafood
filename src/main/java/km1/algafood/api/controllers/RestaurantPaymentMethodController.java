package km1.algafood.api.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import km1.algafood.api.assemblers.PaymentMethodModelAssembler;
import km1.algafood.api.models.PaymentMethodModel;
import km1.algafood.domain.services.RestaurantRegisterService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/restaurants/{restaurantId}/payment-method")
@AllArgsConstructor
public class RestaurantPaymentMethodController {

  private final RestaurantRegisterService rRegisterService;
  private final PaymentMethodModelAssembler pmModelAssembler;

  @GetMapping
  public List<PaymentMethodModel> list(@PathVariable Long restaurantId){
    return  (List<PaymentMethodModel>) pmModelAssembler.toCollectionModel(rRegisterService.fetchByID(restaurantId).getPaymentMethod());

  }

  @PutMapping("/{paymentMethodId}")
  public void associate(@PathVariable Long restaurantId, @PathVariable Long paymentMethodId){
    rRegisterService.associatePaymentMethod(restaurantId, paymentMethodId);

  }

  @DeleteMapping("/{paymentMethodId}")
  public void dsassociate(@PathVariable Long restaurantId, @PathVariable Long paymentMethodId){
    rRegisterService.desassociatePaymentMethod(restaurantId, paymentMethodId);

  }
}
