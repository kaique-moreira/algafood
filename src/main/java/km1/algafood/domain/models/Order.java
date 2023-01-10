package km1.algafood.domain.models;

import java.math.BigDecimal;

public class Order {
	private Long id;
	private BigDecimal subtotal;
	private BigDecimal shippingFee;
	private BigDecimal total;
  public Order() {
  }
  public Order(Long id, BigDecimal subtotal, BigDecimal shippingFee, BigDecimal total) {
    this.id = id;
    this.subtotal = subtotal;
    this.shippingFee = shippingFee;
    this.total = total;
  }
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public BigDecimal getSubtotal() {
    return subtotal;
  }
  public void setSubtotal(BigDecimal subtotal) {
    this.subtotal = subtotal;
  }
  public BigDecimal getShippingFee() {
    return shippingFee;
  }
  public void setShippingFee(BigDecimal shippingFee) {
    this.shippingFee = shippingFee;
  }
  public BigDecimal getTotal() {
    return total;
  }
  public void setTotal(BigDecimal total) {
    this.total = total;
  }

}
