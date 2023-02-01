package km1.algafood.core.modelMapper;

import km1.algafood.api.models.AddresModel;
import km1.algafood.domain.models.Addres;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

  @Bean
  public ModelMapper modelMapper() {
    var modelMapper = new ModelMapper();
    var addresTypeMap = modelMapper().createTypeMap(Addres.class, AddresModel.class);
    addresTypeMap.<String>addMapping(
        source -> source.getCity().getState().getName(),
        (target, value) -> target.getCity().setState(value));
    return modelMapper;
  }
}
