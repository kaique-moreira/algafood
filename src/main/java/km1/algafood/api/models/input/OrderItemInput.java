package km1.algafood.api.models.input;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
public class OrderItemInput {
  @NotNull
  private Long productId;
  @NotNull
  @PositiveOrZero
  private Integer quantity;
  private String note;
}

