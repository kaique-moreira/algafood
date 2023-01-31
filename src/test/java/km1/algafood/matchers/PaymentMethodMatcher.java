package km1.algafood.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import km1.algafood.domain.models.PaymentMethod;


public class PaymentMethodMatcher extends TypeSafeMatcher<PaymentMethod> {

  private PaymentMethod expected;
  public PaymentMethodMatcher(PaymentMethod expected) {
    this.expected = expected;
  } 

  @Override
  public void describeTo(Description description) {
    description.appendText("expected city: " + expected);
  }

  @Override
  protected boolean matchesSafely(PaymentMethod item) {
    boolean isEqualIds = expected.getId() == item.getId();
    boolean isEqualDescriptions = expected.getDescription().equals(item.getDescription());
    return isEqualIds && isEqualDescriptions;
  }

  public static PaymentMethodMatcher isPaymentMethodEqualTo(PaymentMethod expected){
    return new PaymentMethodMatcher(expected);
  }
}
