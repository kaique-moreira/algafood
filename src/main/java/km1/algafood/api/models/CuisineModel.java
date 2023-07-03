package km1.algafood.api.models;


import com.fasterxml.jackson.annotation.JsonView;

import km1.algafood.api.models.view.RestaurantView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuisineModel {
  @JsonView({RestaurantView.Summary.class})
  private Long id;
  @JsonView({RestaurantView.Summary.class})
  private String name;
}
