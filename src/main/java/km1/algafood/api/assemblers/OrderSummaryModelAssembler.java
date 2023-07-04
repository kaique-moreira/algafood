package km1.algafood.api.assemblers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import km1.algafood.api.models.OrderSummaryModel;
import km1.algafood.domain.models.Order;

@Component
public class OrderSummaryModelAssembler {

  private final ModelMapper modelMapper;

  public OrderSummaryModelAssembler(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public OrderSummaryModel toModel(Order source) {
    return modelMapper.map(source, OrderSummaryModel.class);
  }

  public List<OrderSummaryModel> toCollectionModel(Collection<Order> sourceCollection) {
    return sourceCollection.stream()
        .map(source -> modelMapper.map(source, OrderSummaryModel.class))
        .collect(Collectors.toList());
  }
}



