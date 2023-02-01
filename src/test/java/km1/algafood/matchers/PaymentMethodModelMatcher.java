package km1.algafood.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import km1.algafood.api.models.PaymentMethodModel;

public class PaymentMethodModelMatcher extends TypeSafeMatcher<PaymentMethodModel> {

  private PaymentMethodModel expected;
  public PaymentMethodModelMatcher(PaymentMethodModel expected) {
    this.expected = expected;
  } 

  @Override
  public void describeTo(Description description) {
    description.appendText("expected city: " + expected);
  }

  @Override
  protected boolean matchesSafely(PaymentMethodModel item) {
    boolean isEqualIds = expected.getId() == item.getId();
    boolean isEqualDescriptions = expected.getDescription().equals(item.getDescription());
    return isEqualIds && isEqualDescriptions;
  }

  public static PaymentMethodModelMatcher isPaymentMethodModelEqualTo(PaymentMethodModel expected){
    return new PaymentMethodModelMatcher(expected);
  }
}
