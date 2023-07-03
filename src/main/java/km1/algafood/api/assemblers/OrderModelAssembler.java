package km1.algafood.api.assemblers;

import java.util.Collection;
import java.util.stream.Collectors;
import km1.algafood.api.models.OrderModel;
import km1.algafood.domain.models.Order;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderModelAssembler {

  private final ModelMapper modelMapper;

  public OrderModelAssembler(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public OrderModel toModel(Order source) {
    return modelMapper.map(source, OrderModel.class);
  }

  public Collection<OrderModel> toCollectionModel(Collection<Order> sourceCollection) {
    return sourceCollection.stream()
        .map(source -> modelMapper.map(source, OrderModel.class))
        .collect(Collectors.toList());
  }
}



