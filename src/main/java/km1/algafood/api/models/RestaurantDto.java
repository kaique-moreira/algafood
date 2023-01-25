package km1.algafood.api.models;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RestaurantDto {
  private Long id;
  private String name;
  private BigDecimal shippingFee;
  private CuisineDto cuisine;
}
