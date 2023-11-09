package km1.algafood.api.models;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantDto {
  private Long id;
  private String name;
  private BigDecimal shippingFee;
  private CuisineDto cuisine;
}
