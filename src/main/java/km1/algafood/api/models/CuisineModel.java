package km1.algafood.api.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuisineModel {
  private Long id;
  private String name;
  private List<RestaurantModel> restaurants;
}