package km1.algafood.domain.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RestaurantTests {

  Restaurant underTest;

  @BeforeEach
  void setup() {
    underTest = Restaurant.builder().build();
  }

  @Test
  void shouldBuildDefaultRestaurant() {
    assertTrue(underTest.getActive());
    assertTrue(underTest.getOpened());
    assertTrue(underTest.getPaymentMethod() != null);
    assertEquals(0l, underTest.getPaymentMethod().size());
    assertTrue(underTest.getProducts() != null);
    assertEquals(0l, underTest.getProducts().size());
    assertTrue(underTest.getMembers() != null);
    assertEquals(0l, underTest.getMembers().size());
  }

  @Test
  void shouldAddPaymentMethod() {
    var paymentMethod = PaymentMethod.builder().build();
    assertTrue(underTest.addPaymentMethod(paymentMethod));
    assertEquals(1l, underTest.getPaymentMethod().size());
  }

  @Test
  void shouldRemovePaymentMethod() {
    var paymentMethod = PaymentMethod.builder().id(1l).description("").build();
    underTest.addPaymentMethod(paymentMethod);
    assertTrue(underTest.removePaymentMethod(paymentMethod));
    assertFalse(underTest.getPaymentMethod().contains(paymentMethod));
  }

  @Test
  void shouldAddProduct() {
    var product = Product.builder().build();
    assertTrue(underTest.addProduct(product));
    assertEquals(1l, underTest.getProducts().size());
  }

  @Test
  void shouldRemoveProduct() {
    var paymentMethod = Product.builder().build();
    underTest.addProduct(paymentMethod);
    assertTrue(underTest.removeProduct(paymentMethod));
    assertFalse(underTest.getProducts().contains(paymentMethod));
  }

  @Test
  void shouldActive() {
    underTest = Restaurant.builder().active(false).build();
    underTest.active();
    assertTrue(underTest.getActive());
  }

  @Test
  void shouldDisactive() {
    underTest.disactive();
    assertFalse(underTest.getActive());
  }

  @Test
  void shouldOpen() {
    underTest = Restaurant.builder().opened(false).build();
    underTest.open();
    assertTrue(underTest.getOpened());
  }

  @Test
  void shouldClose() {
    underTest.close();
    assertFalse(underTest.getOpened());
  }

  @Test
  void shouldAddMember() {
    var member = User.builder().build();
    assertTrue(underTest.addMember(member));
    assertEquals(1l, underTest.getMembers().size());
  }

  @Test
  void shouldRemoveMember() {
    var member = User.builder().build();
    underTest.addMember(member);
    assertTrue(underTest.removeMember(member));
    assertEquals(0l, underTest.getMembers().size());
  }

  @Test
  void shouldAcceptPaymentMethod() {
    var paymentMethod = PaymentMethod.builder().build();
    underTest.addPaymentMethod(paymentMethod);
    assertTrue(underTest.acceptPaymentMethod(paymentMethod));
  }

  @Test
  void shouldRejectPaymentMethod() {
    var paymentMethod = PaymentMethod.builder().build();
    assertFalse(underTest.acceptPaymentMethod(paymentMethod));
  }
}
