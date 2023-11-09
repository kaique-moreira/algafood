package km1.algafood.api.assemblers;

import java.util.function.Function;
import km1.algafood.api.models.PaymentMethodModel;
import km1.algafood.domain.models.PaymentMethod;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PaymentMethodModelAssembler implements Function<PaymentMethod, PaymentMethodModel> {

  private final ModelMapper modelMapper;

  @Override
  public PaymentMethodModel apply(PaymentMethod t) {
    return modelMapper.map(t, PaymentMethodModel.class);
  }
}
