package km1.algafood.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import km1.algafood.api.models.CuisineModel;

public class CuisineModelMatcher extends TypeSafeMatcher<CuisineModel> {

  private CuisineModel expected;

  public CuisineModelMatcher(CuisineModel expected) {
    this.expected = expected;
  } 

  @Override
  public void describeTo(Description description) {
    description.appendText("expected cuisineModel: " + expected);
  }

  @Override
  protected boolean matchesSafely(CuisineModel item) {
    boolean isEqualIds = expected.getId() == item.getId();
    boolean isEqualNames = expected.getName().equals(item.getName());
    return  isEqualNames && isEqualIds;
  }

  public static CuisineModelMatcher isCuisineModelEqualTo(CuisineModel expected){
    return new CuisineModelMatcher(expected);
  }
  
}
