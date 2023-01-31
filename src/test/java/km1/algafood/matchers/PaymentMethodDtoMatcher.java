package km1.algafood.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import km1.algafood.api.models.PaymentMethodDto;

public class PaymentMethodDtoMatcher extends TypeSafeMatcher<PaymentMethodDto> {

  private PaymentMethodDto expected;
  public PaymentMethodDtoMatcher(PaymentMethodDto expected) {
    this.expected = expected;
  } 

  @Override
  public void describeTo(Description description) {
    description.appendText("expected city: " + expected);
  }

  @Override
  protected boolean matchesSafely(PaymentMethodDto item) {
    boolean isEqualIds = expected.getId() == item.getId();
    boolean isEqualDescriptions = expected.getDescription().equals(item.getDescription());
    return isEqualIds && isEqualDescriptions;
  }

  public static PaymentMethodDtoMatcher isPaymentMethodDtoEqualTo(PaymentMethodDto expected){
    return new PaymentMethodDtoMatcher(expected);
  }
}
