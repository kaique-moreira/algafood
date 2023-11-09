package km1.algafood.api.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StateDto {
  private Long id;
  private String name;
}
