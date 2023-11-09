package km1.algafood.api.assemblers;

import java.util.function.Function;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import km1.algafood.api.models.PaymentMethodInput;
import km1.algafood.domain.models.PaymentMethod;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PaymentMethodInputDisassembler implements Function<PaymentMethodInput, PaymentMethod> {

  private final ModelMapper modelMapper;

  @Override
  public PaymentMethod apply(PaymentMethodInput cityInput) {
    return modelMapper.map(cityInput, PaymentMethod.class);
  }
}
