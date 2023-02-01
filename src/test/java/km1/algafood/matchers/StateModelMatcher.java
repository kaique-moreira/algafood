package km1.algafood.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import km1.algafood.api.models.StateModel;

public class StateModelMatcher extends TypeSafeMatcher<StateModel> {

  private StateModel expected;
  public StateModelMatcher(StateModel expected) {
    this.expected = expected;
  } 

  @Override
  public void describeTo(Description description) {
    description.appendText("expected state: " + expected);
  }

  @Override
  protected boolean matchesSafely(StateModel item) {
    boolean isEqualIds = expected.getId() == item.getId();
    boolean isEqualNames = expected.getName().equals(item.getName());
    return isEqualIds && isEqualNames;
  }

  public static StateModelMatcher isStateModelEqualTo(StateModel expected){
    return new StateModelMatcher(expected);
  }
  
}
