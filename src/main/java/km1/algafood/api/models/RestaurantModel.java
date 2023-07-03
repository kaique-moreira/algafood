package km1.algafood.api.models;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonView;

import km1.algafood.api.models.view.RestaurantView;
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
  @JsonView({RestaurantView.Summary.class, RestaurantView.OnlyName.class})
  private Long id;
  @JsonView({RestaurantView.Summary.class, RestaurantView.OnlyName.class})
  private String name;
  @JsonView({RestaurantView.Summary.class})
  private BigDecimal shippingFee;
  @JsonView({RestaurantView.Summary.class})
  private CuisineModel cuisine;
  private Boolean active;
  private Boolean opened;
  private AddresModel addres;
}
