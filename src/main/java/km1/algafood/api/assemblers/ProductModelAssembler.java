package km1.algafood.api.assemblers;

import java.util.Collection;
import java.util.stream.Collectors;
import km1.algafood.api.models.ProductModel;
import km1.algafood.domain.models.Product;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductModelAssembler {

  private final ModelMapper modelMapper;

  public ProductModelAssembler(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public ProductModel toModel(Product source) {
    return modelMapper.map(source, ProductModel.class);
  }

  public Collection<ProductModel> toCollectionModel(Collection<Product> sourceCollection) {
    return sourceCollection.stream()
        .map(source -> modelMapper.map(source, ProductModel.class))
        .collect(Collectors.toList());
  }
}

