package km1.algafood.api.models;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CuisineDto {
  private Long id;
  private String name;
  private List<RestaurantDto> restaurants;
  
}
