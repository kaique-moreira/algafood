package km1.algafood.utils;

import km1.algafood.api.models.PaymentMethodDto;
import km1.algafood.api.models.PaymentMethodInput;
import km1.algafood.domain.models.PaymentMethod;

public class PaymentMethodTestBuilder {
  private static final long VALID_ID = 1l;
  private PaymentMethod cuisine = PaymentMethod.builder().description("Francesa").build();

  public static PaymentMethodTestBuilder aPaymentMethod() {
    return new PaymentMethodTestBuilder();
  }

  public PaymentMethodTestBuilder withValidId() {
    this.cuisine.setId(VALID_ID);
    return this;
  }

  public PaymentMethodTestBuilder withNullDescription() {
    this.cuisine.setDescription(null);
    return this;
  }

  public PaymentMethod build() {
    return this.cuisine;
  }

  public PaymentMethodInput buildInput() {
    return PaymentMethodInput.builder()
    .description(this.cuisine.getDescription())
    .build();
  }

  public PaymentMethodDto buildDto() {
    return PaymentMethodDto.builder()
    .description(this.cuisine.getDescription())
    .id(this.cuisine.getId())
    .build();
  }
}
