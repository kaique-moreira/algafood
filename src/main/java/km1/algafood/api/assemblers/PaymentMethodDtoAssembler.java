package km1.algafood.api.assemblers;

import java.util.function.Function;
import km1.algafood.api.models.PaymentMethodDto;
import km1.algafood.domain.models.PaymentMethod;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PaymentMethodDtoAssembler implements Function<PaymentMethod, PaymentMethodDto> {

  private final ModelMapper modelMapper;

  @Override
  public PaymentMethodDto apply(PaymentMethod t) {
    return modelMapper.map(t, PaymentMethodDto.class);
  }
}
