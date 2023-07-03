package km1.algafood.api.models.input;

import java.math.BigDecimal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantInput {
  @NotBlank
  @EqualsAndHashCode.Include
  private String name;
  @NotNull
  @PositiveOrZero
  private BigDecimal shippingFee;
  @Valid
  @NotNull
  private CuisineIdInput cuisineIdInput;
  @Valid
  @NotNull
  private AddresInput addresiInput;
}
