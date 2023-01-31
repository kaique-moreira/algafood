package km1.algafood.api.models;

import java.math.BigDecimal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantInput {
  @NotBlank
  private String name;
  @NotNull
  @PositiveOrZero
  private BigDecimal shippingFee;
  @Valid
  @NotNull
  private CuisineIdInput cuisineIdInput;
}
