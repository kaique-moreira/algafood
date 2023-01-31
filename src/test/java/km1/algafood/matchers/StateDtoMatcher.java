package km1.algafood.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import km1.algafood.api.models.StateDto;

public class StateDtoMatcher extends TypeSafeMatcher<StateDto> {

  private StateDto expected;
  public StateDtoMatcher(StateDto expected) {
    this.expected = expected;
  } 

  @Override
  public void describeTo(Description description) {
    description.appendText("expected state: " + expected);
  }

  @Override
  protected boolean matchesSafely(StateDto item) {
    boolean isEqualIds = expected.getId() == item.getId();
    boolean isEqualNames = expected.getName().equals(item.getName());
    return isEqualIds && isEqualNames;
  }

  public static StateDtoMatcher isStateDtoEqualTo(StateDto expected){
    return new StateDtoMatcher(expected);
  }
  
}
