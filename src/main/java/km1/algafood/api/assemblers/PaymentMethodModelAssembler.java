package km1.algafood.api.assemblers;

import java.util.Collection;
import java.util.stream.Collectors;
import km1.algafood.api.models.PaymentMethodModel;
import km1.algafood.domain.models.PaymentMethod;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PaymentMethodModelAssembler {

  private final ModelMapper modelMapper;

  public PaymentMethodModelAssembler(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public PaymentMethodModel toModel(PaymentMethod source) {
    return modelMapper.map(source, PaymentMethodModel.class);
  }

  public Collection<PaymentMethodModel> toCollectionModel(Collection<PaymentMethod> sourceCollection) {
    return sourceCollection.stream()
        .map(source -> modelMapper.map(source, PaymentMethodModel.class))
        .collect(Collectors.toList());
  }
}


