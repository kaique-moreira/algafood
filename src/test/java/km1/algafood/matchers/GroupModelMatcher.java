package km1.algafood.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import km1.algafood.api.models.GroupModel;

public class GroupModelMatcher extends TypeSafeMatcher<GroupModel> {

  private GroupModel expected;
  public GroupModelMatcher(GroupModel expected) {
    this.expected = expected;
  } 

  @Override
  public void describeTo(Description description) {
    description.appendText("expected city: " + expected);
  }

  @Override
  protected boolean matchesSafely(GroupModel item) {
    boolean isEqualNames = expected.getName().equals(item.getName());
    return isEqualNames;
  }

  public static GroupModelMatcher isGroupModelEqualTo(GroupModel expected){
    return new GroupModelMatcher(expected);
  }
}
