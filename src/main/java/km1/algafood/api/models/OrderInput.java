package km1.algafood.api.models;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class OrderInput {
  @NotNull
  private AddresModel deliveryAddres;
  @NotNull
  @Valid
  private PaymentMethodIdInput paymentMethod;
  @NotNull
  @Valid
  private RestaurantIdInput restaurant;
  @Valid
  @Size(min = 1)
  @NotNull
  private List<OrderItemInput> itens;
}
