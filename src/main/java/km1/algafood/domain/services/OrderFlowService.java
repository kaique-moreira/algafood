package km1.algafood.domain.services;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderFlowService {
  private final OrderIssuenceService orderIssuenceService;

  @Transactional
  public void confirm(String orderCode) {
    var order = orderIssuenceService.fetchByCode(orderCode);
    order.confirm();
  }

  @Transactional
  public void cancel(String orderCode) {
    var order = orderIssuenceService.fetchByCode(orderCode);
    order.cancel();
  }

  @Transactional
  public void delivery(String orderCode) {
    var order = orderIssuenceService.fetchByCode(orderCode);
    order.delivery();
  }
}
