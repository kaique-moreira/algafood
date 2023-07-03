package km1.algafood.api.models;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import km1.algafood.domain.models.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryModel {
  private String code;
  private BigDecimal subtotal;
  private BigDecimal shippingFee;
  private BigDecimal total;
  private OrderStatus status;
  private OffsetDateTime confimedDate;
  private RestaurantSummaryModel restaurante;
  private UserModel client;

}
