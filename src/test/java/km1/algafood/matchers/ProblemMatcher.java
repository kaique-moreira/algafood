package km1.algafood.matchers;

import static km1.algafood.utils.ProblemBuilderFactory.*;

import km1.algafood.api.exceptionHandler.Problem;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class ProblemMatcher extends TypeSafeMatcher<Problem> {

  private Problem expected;

  public ProblemMatcher(Problem problem) {
    this.expected = problem;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("expected problem: " + expected);
  }

  @Override
  protected boolean matchesSafely(Problem actual) {

    boolean isEqualTypes = expected.getType().equals(actual.getType());
    boolean isEqualTitles = expected.getTitle().equals(actual.getTitle());
    boolean isEqualStatus = expected.getStatus() == actual.getStatus();

    return (isEqualTypes && isEqualTitles && isEqualStatus);
  }

  public static ProblemMatcher isNotFoundProblem() {
    return new ProblemMatcher(notFoundProblem().build());
  }

  public static ProblemMatcher isBadRequest() {
    return new ProblemMatcher(notFoundProblem().build());
  }

  public static ProblemMatcher isConflictProblem() {
    return new ProblemMatcher(conflictProblem().build());
  }
}
