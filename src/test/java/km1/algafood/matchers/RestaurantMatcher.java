package km1.algafood.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import km1.algafood.domain.models.Restaurant;

public class RestaurantMatcher extends TypeSafeMatcher<Restaurant> {

  private Restaurant expected;
  public RestaurantMatcher(Restaurant expected) {
    this.expected = expected;
  } 

  @Override
  public void describeTo(Description description) {
    description.appendText("expected restaurant: " + expected);
  }

  @Override
  protected boolean matchesSafely(Restaurant item) {
    boolean isEqualIds = expected.getId() == item.getId();
    boolean isEqualNames = expected.getName() == item.getName();
    return isEqualIds && isEqualNames;
  }

  public static RestaurantMatcher isRestaurantEqualTo(Restaurant expected){
    return new RestaurantMatcher(expected);
  }
  
}
