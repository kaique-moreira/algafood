package km1.algafood.utils;

import km1.algafood.api.models.CuisineModel;
import km1.algafood.api.models.CuisineInput;
import km1.algafood.domain.models.Cuisine;

public class CuisineTestBuilder {
  private static final long VALID_ID = 1l;
  private Cuisine cuisine = Cuisine.builder().name("Francesa").build();

  public static CuisineTestBuilder aCuisine() {
    return new CuisineTestBuilder();
  }

  public CuisineTestBuilder withValidId() {
    this.cuisine.setId(VALID_ID);
    return this;
  }

  public CuisineTestBuilder withNullName() {
    this.cuisine.setName(null);
    return this;
  }

  public Cuisine build() {
    return this.cuisine;
  }

  public CuisineInput buildInput() {
    return CuisineInput.builder()
    .name(this.cuisine.getName())
    .build();
  }

  public CuisineModel buildModel() {
    return CuisineModel.builder()
    .name(this.cuisine.getName())
    .id(this.cuisine.getId())
    .build();
  }
}
