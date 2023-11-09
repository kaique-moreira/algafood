package km1.algafood.matchers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import km1.algafood.api.models.UserModel;

public class UserModelMatcher extends TypeSafeMatcher<UserModel> {

  private UserModel expected;

  public UserModelMatcher(UserModel expected) {
    this.expected = expected;
  } 

  @Override
  public void describeTo(Description description) {
    description.appendText("expected user: " + expected);
  }

  @Override
  protected boolean matchesSafely(UserModel item) {
    List<Predicate<UserModel>> predicates = new ArrayList<>();
    predicates.add((expected) -> expected.getId().equals(item.getId()));
    predicates.add((expected) -> expected.getName().equals(item.getName()));
    predicates.add((expected) -> expected.getEmail().equals(item.getEmail()));
    
    return predicates.stream().reduce((x) -> true, (acc,predicate) -> acc.and(predicate)).test(expected);
  }

  public static UserModelMatcher isUserModelEqualTo(UserModel expected){
    return new UserModelMatcher(expected);
  }
  
}
