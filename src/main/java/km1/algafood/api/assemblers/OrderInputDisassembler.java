package km1.algafood.api.assemblers;

import java.util.Collection;
import java.util.stream.Collectors;
import km1.algafood.api.models.OrderInput;
import km1.algafood.domain.models.Order;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderInputDisassembler {

  private final ModelMapper modelMapper;

  public OrderInputDisassembler(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public Order toDomainObject(OrderInput source) {
    return modelMapper.map(source, Order.class);
  }

  public Collection<Order> toCollectionDomainObject(Collection<OrderInput> sourceCollection) {
    return sourceCollection.stream()
        .map(source -> modelMapper.map(source, Order.class))
        .collect(Collectors.toList());
  }
}
