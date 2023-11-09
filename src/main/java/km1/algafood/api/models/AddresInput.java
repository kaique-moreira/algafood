package km1.algafood.api.models;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddresInput {

  @NotBlank
  private String postalCode;

  @NotBlank
  private String street;

  @NotBlank
  private String number;

  private String complement;

  @NotBlank
  private String district;

  @Valid
  private CityIdInput city; 
}
