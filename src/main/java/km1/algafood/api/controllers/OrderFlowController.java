package km1.algafood.api.controllers;

import km1.algafood.domain.services.OrderFlowService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders/{orderCode}")
@AllArgsConstructor
public class OrderFlowController {
  private final OrderFlowService orderFlowService;

  @PutMapping("/confirm")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void confirm(@PathVariable String orderCode) {
    orderFlowService.confirm(orderCode);
  }

  @PutMapping("/cancel")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void cancel(@PathVariable String orderCode) {
    orderFlowService.cancel(orderCode);
  }

  @PutMapping("/delivery")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delivery(@PathVariable String orderCode) {
    orderFlowService.delivery(orderCode);
  }
}
