package km1.algafood.domain.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lowagie.text.pdf.AcroFields.Item;

import km1.algafood.domain.exceptions.DomainException;

public class OrderTests {
  Order.OrderBuilder builder;
  Order underTest;

  @BeforeEach
  void setup() {
    builder = Order.builder();
  }

  @Test
  void shouldChangeStatusToConfirmed_WhenConfirmCreatedOrder() {
    underTest = builder.build();
    underTest.confirm();
    assertEquals(OrderStatus.CONFIRMED, underTest.getStatus());
    assertTrue(underTest.getConfirmDate() != null);
  }

  @Test
  void shouldDoNothing_WhenConfirmConfirmedOrder() {
    var now = OffsetDateTime.now();
    var underTest = builder.status(OrderStatus.CONFIRMED).confirmDate(now).build();
    underTest.confirm();
    assertEquals(OrderStatus.CONFIRMED, underTest.getStatus());
    assertEquals(now, underTest.getConfirmDate());
  }

  @Test
  void shouldThrowDomainExeption_WhenConfirmCanceled() {
    underTest = builder.status(OrderStatus.CANCELED).build();
    assertThrows(DomainException.class, () -> underTest.confirm());
  }

  @Test
  void shouldThrowDomainExeption_WhenConfirmDelivered() {
    underTest = builder.status(OrderStatus.DELIVERED).build();
    assertThrows(DomainException.class, () -> underTest.confirm());
  }

  @Test
  void shouldChangeStatusToDelivered_WhenDeliveryConfirmedOrder() {
    underTest = builder.status(OrderStatus.CONFIRMED).build();
    underTest.delivery();
    assertEquals(OrderStatus.DELIVERED, underTest.getStatus());
  }

  @Test
  void shouldDoNothing_WhenDeliveryDeliveredOrder() {
    var now = OffsetDateTime.now();
    var underTest = builder.status(OrderStatus.DELIVERED).deliveryDate(now).build();
    underTest.delivery();
    assertEquals(OrderStatus.DELIVERED, underTest.getStatus());
    assertEquals(now, underTest.getDeliveryDate());
  }

  @Test
  void shouldThrowDomainExeption_WhenDeleveryCreatedOrder() {
    underTest = builder.status(OrderStatus.CREATED).build();
    assertThrows(DomainException.class, () -> underTest.delivery());
  }

  @Test
  void should_ThrowDomainExeption_WhenDeleveryCanceledOrder() {
    underTest = builder.status(OrderStatus.CANCELED).build();
    assertThrows(DomainException.class, () -> underTest.delivery());
  }

  @Test
  void shouldChangeStatusToCanceled_WhenCancelCreatedOrder() {
    underTest = builder.status(OrderStatus.CREATED).build();
    underTest.cancel();
    assertEquals(OrderStatus.CANCELED, underTest.getStatus());
  }

  @Test
  void shouldDoNothing_WhenCancelCanceledOrder() {
    var now = OffsetDateTime.now();
    var underTest = builder.status(OrderStatus.CANCELED).cancelDate(now).build();
    underTest.cancel();
    assertEquals(OrderStatus.CANCELED, underTest.getStatus());
    assertEquals(now, underTest.getCancelDate());
  }

  @Test
  void should_ThrowDomainExeption_WhenCancelDeliveredOrder() {
    underTest = builder.status(OrderStatus.DELIVERED).build();
    assertThrows(DomainException.class, () -> underTest.cancel());
  }

  @Test
  void should_ThrowDomainExeption_WhenCancelConfirmedOrder() {
    underTest = builder.status(OrderStatus.CONFIRMED).build();
    assertThrows(DomainException.class, () -> underTest.cancel());
  }

  @Test
  void shouldCalculateTotal() {
    underTest = builder.itens(List.of(OrderItem.builder().quantity(10).unityPrice(BigDecimal.valueOf(100l)).build(),
        OrderItem.builder().quantity(10).unityPrice(BigDecimal.valueOf(100l)).build())).build();
    underTest.calculateTotal();
    assertEquals(BigDecimal.valueOf(2000l), underTest.getTotal());

  }

  @Test
  void shouldCalculateTotal1() {
    underTest = builder.itens(List.of(OrderItem.builder().quantity(10).build()))
        .build();

    assertThrows(DomainException.class, () -> underTest.calculateTotal());
  }

  @Test
  void shouldCalculateTotal4() {
    underTest = builder.itens(List.of(OrderItem.builder().unityPrice(BigDecimal.ZERO).quantity(10).build()))
        .build();

    assertThrows(DomainException.class, () -> underTest.calculateTotal());
  }

  @Test
  void shouldCalculateTotal3() {
    underTest = builder.itens(List.of(OrderItem.builder().quantity(0).unityPrice(BigDecimal.valueOf(10l)).build()))
        .build();
    assertThrows(DomainException.class, () -> underTest.calculateTotal());
  }

  @Test
  void shouldCalculateTotal2() {
    underTest = builder.itens(List.of(OrderItem.builder().unityPrice(BigDecimal.valueOf(10l)).build()))
        .build();
    assertThrows(DomainException.class, () -> underTest.calculateTotal());
  }

  @Test
  void shouldCalculateTotalShiPi() {
    underTest = builder.itens(List.of(OrderItem.builder().quantity(10).unityPrice(BigDecimal.valueOf(100l)).build(),
        OrderItem.builder().quantity(10).unityPrice(BigDecimal.valueOf(100l)).build()))
        .shippingFee(BigDecimal.valueOf(100l)).build();
    underTest.calculateTotal();
    assertEquals(BigDecimal.valueOf(2100l), underTest.getTotal());

  }

  @Test
  void shouldGenerateCode() throws NoSuchMethodException, SecurityException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    underTest = builder.build();
    Method method = Order.class.getDeclaredMethod("generateCode");
    method.setAccessible(true);
    method.invoke(underTest);
    assertTrue(underTest.getCode() != null);
  }
}
