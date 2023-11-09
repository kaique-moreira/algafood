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
public class RestaurantDto {
  private Long id;
  private String name;
  private BigDecimal shippingFee;
  private CuisineDto cuisine;
  private Boolean active;
}
