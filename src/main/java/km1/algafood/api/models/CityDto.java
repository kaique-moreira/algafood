package km1.algafood.api.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityDto {
  private Long id; 
  private String name;
  private StateDto state;
}
