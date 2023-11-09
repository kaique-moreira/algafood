package km1.algafood.domain.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_order_item")
public class OrderItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private BigDecimal unityPrice;
  private BigDecimal totalPrice;
  private Integer quantity;
  private String note;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Order order;
  
  @ManyToOne
  @JoinColumn(nullable = false)
  private Product product;

  public OrderItem() {}

  public OrderItem(
      Long id,
      BigDecimal unityPrice,
      BigDecimal totalPrice,
      Integer quantity,
      String note,
      Order order,
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    OrderItem other = (OrderItem) obj;
    if (id == null) {
      if (other.id != null) return false;
    } else if (!id.equals(other.id)) return false;
    return true;
  }
}
