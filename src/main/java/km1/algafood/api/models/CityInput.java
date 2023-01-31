package km1.algafood.api.models;

import lombok.AllArgsConstructor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityInput {
  @NotBlank
  private String name;
  @Valid
  @NotNull
  private StateIdImput state;
}
