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
public class RestaurantModel {
  private Long id;
  private String name;
  private BigDecimal shippingFee;
  private CuisineModel cuisine;
  private Boolean active;
  private AddresModel addresModel;
}
