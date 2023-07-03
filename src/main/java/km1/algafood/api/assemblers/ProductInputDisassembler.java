package km1.algafood.api.assemblers;

import java.util.Collection;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import km1.algafood.api.models.input.ProductInput;
import km1.algafood.domain.models.Product;

@Component
public class ProductInputDisassembler {

  private final ModelMapper modelMapper;

  public ProductInputDisassembler(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public Product toDomainObject(ProductInput source) {
    return modelMapper.map(source, Product.class);
  }

  public Collection<Product> toCollectionDomainObject(Collection<ProductInput> sourceCollection) {
    return sourceCollection.stream()
        .map(source -> modelMapper.map(source, Product.class))
        .collect(Collectors.toList());
  }
}


