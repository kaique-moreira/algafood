package km1.algafood.utils;

import km1.algafood.domain.models.Product;

public class ProductTestBuilder {
  private Product product =
      Product.builder()
          .build();

  public static ProductTestBuilder aProduct() {
    return new ProductTestBuilder();
  }


  public Product build() {
    return this.product;
  }

}
