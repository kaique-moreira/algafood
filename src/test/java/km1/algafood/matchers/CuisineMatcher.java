package km1.algafood.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import km1.algafood.domain.models.Cuisine;

public class CuisineMatcher extends TypeSafeMatcher<Cuisine> {

  private Cuisine expected;

  public CuisineMatcher(Cuisine expected) {
    this.expected = expected;
  } 

  @Override
  public void describeTo(Description description) {
    description.appendText("expected cuisine: " + expected);
  }

  @Override
  protected boolean matchesSafely(Cuisine item) {
    boolean isEqualIds = expected.getId() == item.getId();
    boolean isEqualNames = expected.getName() == item.getName();
    return isEqualIds && isEqualNames;
  }

  public static CuisineMatcher isCuisineEqualTo(Cuisine expected){
    return new CuisineMatcher(expected);
  }
  
}
