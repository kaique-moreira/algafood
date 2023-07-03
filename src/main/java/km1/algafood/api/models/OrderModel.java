package km1.algafood.api.models;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

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
public class OrderModel {
  private String code;
  private BigDecimal subtotal;
  private BigDecimal shippingFee;
  private BigDecimal total;
  private AddresModel deliveryAddres;
  private OrderStatus status;
  private OffsetDateTime confimedDate;
  private OffsetDateTime cancelDate;
  private OffsetDateTime deliveryDate;
  private PaymentMethodModel paymentMethod;
  private RestaurantSummaryModel restaurant;
  private UserModel client;
  @Builder.Default
  private List<OrderItemModel> itens = new ArrayList<>();

}
