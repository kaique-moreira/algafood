package km1.algafood.api.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CityInput {
  private String name;
  private StateIdImput state;
}
