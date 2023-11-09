package km1.algafood.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import km1.algafood.api.models.CityDto;

public class CityDtoMatcher extends TypeSafeMatcher<CityDto> {

  private CityDto expected;
  public CityDtoMatcher(CityDto expected) {
    this.expected = expected;
  } 

  @Override
  public void describeTo(Description description) {
    description.appendText("expected city: " + expected);
  }

  @Override
  protected boolean matchesSafely(CityDto item) {
    boolean isEqualIds = expected.getId() == item.getId();
    boolean isEqualNames = expected.getName().equals(item.getName());
    return isEqualIds && isEqualNames;
  }

  public static CityDtoMatcher isCityDtoEqualTo(CityDto expected){
    return new CityDtoMatcher(expected);
  }
}
