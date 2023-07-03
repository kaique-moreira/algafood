package km1.algafood.api.models;

import java.math.BigDecimal;

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
public class OrderItemModel {
  private Long productId;
  private String productName;
  private BigDecimal unityPrice;
  private BigDecimal totalPrice;
  private Integer quantity;
  private String note;
}

