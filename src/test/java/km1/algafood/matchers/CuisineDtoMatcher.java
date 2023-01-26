package km1.algafood.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import km1.algafood.api.models.CuisineDto;

public class CuisineDtoMatcher extends TypeSafeMatcher<CuisineDto> {

  private CuisineDto expected;

  public CuisineDtoMatcher(CuisineDto expected) {
    this.expected = expected;
  } 

  @Override
  public void describeTo(Description description) {
    description.appendText("expected cuisineDto: " + expected);
  }

  @Override
  protected boolean matchesSafely(CuisineDto item) {
    boolean isEqualIds = expected.getId() == item.getId();
    boolean isEqualNames = expected.getName().equals(item.getName());
    return  isEqualNames && isEqualIds;
  }

  public static CuisineDtoMatcher isCuisineDtoEqualTo(CuisineDto expected){
    return new CuisineDtoMatcher(expected);
  }
  
}
