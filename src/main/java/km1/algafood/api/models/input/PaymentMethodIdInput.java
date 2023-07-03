package km1.algafood.api.models.input;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentMethodIdInput {
  @NotNull
  private Long id;

}

