package km1.algafood.utils;

import static km1.algafood.utils.CityTestBuilder.aCity;

import km1.algafood.api.models.AddresModel;
import km1.algafood.domain.models.Addres;

public class AddresTestBuilder {
  private Addres addres =
      Addres.builder()
          .city(aCity().build())
          .number("5")
          .street("test")
          .district("test")
          .postalCode("test")
          .complement("test")
          .build();

  public static AddresTestBuilder aAddres() {
    return new AddresTestBuilder();
  }


  public Addres build() {
    return this.addres;
  }

  public AddresModel buildModel() {
    return AddresModel.builder()
          .city(aCity().buildSummary())
          .number(this.addres.getNumber())
          .street(this.addres.getStreet())
          .district(this.addres.getDistrict())
          .postalCode(this.addres.getPostalCode())
          .complement(this.addres.getComplement())
          .build();
  }
}
