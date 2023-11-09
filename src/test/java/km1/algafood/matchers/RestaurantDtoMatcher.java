package km1.algafood.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import km1.algafood.api.models.RestaurantDto;

public class RestaurantDtoMatcher extends TypeSafeMatcher<RestaurantDto> {

  private RestaurantDto expected;
  public RestaurantDtoMatcher(RestaurantDto expected) {
    this.expected = expected;
  } 

  @Override
  public void describeTo(Description description) {
    description.appendText("expected restaurant: " + expected);
  }

  @Override
  protected boolean matchesSafely(RestaurantDto item) {
    boolean isEqualIds = expected.getId() == item.getId();
    boolean isEqualNames = expected.getName().equals(item.getName());
    return isEqualIds && isEqualNames;
  }

  public static RestaurantDtoMatcher isRestaurantDtoEqualTo(RestaurantDto expected){
    return new RestaurantDtoMatcher(expected);
  }
  
}
