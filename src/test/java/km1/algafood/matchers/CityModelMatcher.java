package km1.algafood.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import km1.algafood.api.models.CityModel;

public class CityModelMatcher extends TypeSafeMatcher<CityModel> {

  private CityModel expected;
  public CityModelMatcher(CityModel expected) {
    this.expected = expected;
  } 

  @Override
  public void describeTo(Description description) {
    description.appendText("expected city: " + expected);
  }

  @Override
  protected boolean matchesSafely(CityModel item) {
    boolean isEqualIds = expected.getId() == item.getId();
    boolean isEqualNames = expected.getName().equals(item.getName());
    return isEqualIds && isEqualNames;
  }

  public static CityModelMatcher isCityModelEqualTo(CityModel expected){
    return new CityModelMatcher(expected);
  }
}
