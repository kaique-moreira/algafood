package km1.algafood.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import km1.algafood.domain.models.City;

public class CityMatcher extends TypeSafeMatcher<City> {

  private City expected;
  public CityMatcher(City expected) {
    this.expected = expected;
  } 

  @Override
  public void describeTo(Description description) {
    description.appendText("expected city: " + expected);
  }

  @Override
  protected boolean matchesSafely(City item) {
    boolean isEqualIds = expected.getId() == item.getId();
    boolean isEqualNames = expected.getName() == item.getName();
    return isEqualIds && isEqualNames;
  }

  public static CityMatcher isCityEqualTo(City expected){
    return new CityMatcher(expected);
  }
  
}
