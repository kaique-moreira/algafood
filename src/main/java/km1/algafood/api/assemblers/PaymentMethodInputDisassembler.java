package km1.algafood.api.assemblers;

import java.util.Collection;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import km1.algafood.api.models.input.PaymentMethodInput;
import km1.algafood.domain.models.PaymentMethod;

@Component
public class PaymentMethodInputDisassembler {

  private final ModelMapper modelMapper;

  public PaymentMethodInputDisassembler(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public PaymentMethod toDomainObject(PaymentMethodInput source) {
    return modelMapper.map(source, PaymentMethod.class);
  }

  public Collection<PaymentMethod> toCollectionDomainObject(Collection<PaymentMethodInput> sourceCollection) {
    return sourceCollection.stream()
        .map(source -> modelMapper.map(source, PaymentMethod.class))
        .collect(Collectors.toList());
  }
}



