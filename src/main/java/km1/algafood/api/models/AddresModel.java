package km1.algafood.api.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddresModel {

  private String postalCode;

  private String street;

  private String number;

  private String complement;

  private String district;

  private CitySummary city; 
}
