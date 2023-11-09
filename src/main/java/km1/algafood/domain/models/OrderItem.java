package km1.algafood.domain.models;

import java.math.BigDecimal;

public class OrderItem {
	private Long id;
	private BigDecimal unityPrice;
	private BigDecimal totalPrice;
	private Integer quantity;
	private String note;
	private Order order;
	private Product product;

  public OrderItem() {
  }

  public OrderItem(Long id, BigDecimal unityPrice, BigDecimal totalPrice, Integer quantity, String note, Order order,
      Product product) {
    this.id = id;
    this.unityPrice = unityPrice;
    this.totalPrice = totalPrice;
    this.quantity = quantity;
    this.note = note;
    this.order = order;
    this.product = product;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public BigDecimal getUnityPrice() {
    return unityPrice;
  }

  public void setUnityPrice(BigDecimal unityPrice) {
    this.unityPrice = unityPrice;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }


}
