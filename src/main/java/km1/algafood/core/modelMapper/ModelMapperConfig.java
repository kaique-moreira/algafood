package km1.algafood.core.modelMapper;

import km1.algafood.api.models.AddresModel;
import km1.algafood.api.models.OrderItemInput;
import km1.algafood.domain.models.Addres;
import km1.algafood.domain.models.OrderItem;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

  @Bean
  public ModelMapper modelMapper() {
    var modelMapper = new ModelMapper();
    modelMapper
        .createTypeMap(Addres.class, AddresModel.class)
        .<String>addMapping(
            source -> source.getCity().getState().getName(),
            (target, value) -> target.getCity().setState(value));

    modelMapper
        .createTypeMap(OrderItemInput.class, OrderItem.class)
        .addMappings(mapper -> mapper.skip(OrderItem::setId));

    return modelMapper;
  }
}
