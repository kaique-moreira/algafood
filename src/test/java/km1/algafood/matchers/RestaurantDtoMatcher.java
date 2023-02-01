package km1.algafood.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import km1.algafood.api.models.RestaurantModel;

public class RestaurantDtoMatcher extends TypeSafeMatcher<RestaurantModel> {

  private RestaurantModel expected;
  public RestaurantDtoMatcher(RestaurantModel expected) {
    this.expected = expected;
  } 

  @Override
  public void describeTo(Description description) {
    description.appendText("expected restaurant: " + expected);
  }

  @Override
  protected boolean matchesSafely(RestaurantModel item) {
    boolean isEqualIds = expected.getId() == item.getId();
    boolean isEqualNames = expected.getName().equals(item.getName());
    return isEqualIds && isEqualNames;
  }

  public static RestaurantDtoMatcher isRestaurantDtoEqualTo(RestaurantModel expected){
    return new RestaurantDtoMatcher(expected);
  }
  
}
