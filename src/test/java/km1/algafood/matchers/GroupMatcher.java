package km1.algafood.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import km1.algafood.domain.models.Group;

public class GroupMatcher extends TypeSafeMatcher<Group> {

  private Group expected;
  public GroupMatcher(Group expected) {
    this.expected = expected;
  } 

  @Override
  public void describeTo(Description description) {
    description.appendText("expected city: " + expected);
  }

  @Override
  protected boolean matchesSafely(Group item) {
    boolean isEqualIds = expected.getId() == item.getId();
    boolean isEqualNames = expected.getName() == item.getName();
    return isEqualIds && isEqualNames;
  }

  public static GroupMatcher isGroupEqualTo(Group expected){
    return new GroupMatcher(expected);
  }
}
