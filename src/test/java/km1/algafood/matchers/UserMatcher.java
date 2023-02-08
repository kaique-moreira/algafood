package km1.algafood.matchers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import km1.algafood.domain.models.User;

public class UserMatcher extends TypeSafeMatcher<User> {

  private User expected;

  public UserMatcher(User expected) {
    this.expected = expected;
  } 

  @Override
  public void describeTo(Description description) {
    description.appendText("expected user: " + expected);
  }

  @Override
  protected boolean matchesSafely(User item) {
    List<Predicate<User>> predicates = new ArrayList<>();
    predicates.add((expected) -> expected.getId().equals(item.getId()));
    predicates.add((expected) -> expected.getName().equals(item.getName()));
    predicates.add((expected) -> expected.getEmail().equals(item.getEmail()));
    predicates.add((expected) -> expected.getPassword().equals(item.getPassword()));
    
    return predicates.stream().reduce((x) -> true, (acc,predicate) -> acc.and(predicate)).test(expected);
  }

  public static UserMatcher isUserEqualTo(User expected){
    return new UserMatcher(expected);
  }
  
}
