package km1.algafood.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import km1.algafood.domain.models.State;

public class StateMatcher extends TypeSafeMatcher<State> {

  private State expected;
  public StateMatcher(State expected) {
    this.expected = expected;
  } 

  @Override
  public void describeTo(Description description) {
    description.appendText("expected state: " + expected);
  }

  @Override
  protected boolean matchesSafely(State item) {
    boolean isEqualIds = expected.getId() == item.getId();
    boolean isEqualNames = expected.getName() == item.getName();
    return isEqualIds && isEqualNames;
  }

  public static StateMatcher isStateEqualTo(State expected){
    return new StateMatcher(expected);
  }
  
}
