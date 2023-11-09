package km1.algafood.domain.models;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "tb_order")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private BigDecimal subtotal;
  private BigDecimal shippingFee;
  private BigDecimal total;

  @Embedded private Addres enderecoEntrega;

  private OrderStatus status;
  @CreationTimestamp private LocalDateTime createdDate;
  private LocalDateTime confimedDate;
  private LocalDateTime cancelDate;
  private LocalDateTime deliveryDate;

  @ManyToOne
  @JoinColumn(nullable = false)
  private PaymentMethod formaPagamento;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Restaurant restaurante;

  @ManyToOne
  @JoinColumn(nullable = false)
  private User cliente;

  @OneToMany(mappedBy = "order")
  private List<OrderItem> itens = new ArrayList<>();

  public Order() {}

  public Order(
      Long id,
      BigDecimal subtotal,
      BigDecimal shippingFee,
      BigDecimal total,
      Addres enderecoEntrega,
      OrderStatus status,
      LocalDateTime createdDate,
      LocalDateTime confimedDate,
      LocalDateTime cancelDate,
      LocalDateTime deliveryDate) {
    this.id = id;
    this.subtotal = subtotal;
    this.shippingFee = shippingFee;
    this.total = total;
    this.enderecoEntrega = enderecoEntrega;
    this.status = status;
    this.createdDate = createdDate;
    this.confimedDate = confimedDate;
    this.cancelDate = cancelDate;
    this.deliveryDate = deliveryDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public PaymentMethod getFormaPagamento() {
    return formaPagamento;
  }

  public void setFormaPagamento(PaymentMethod formaPagamento) {
    this.formaPagamento = formaPagamento;
  }

  public Restaurant getRestaurante() {
    return restaurante;
  }

  public void setRestaurante(Restaurant restaurante) {
    this.restaurante = restaurante;
  }

  public User getCliente() {
    return cliente;
  }

  public void setCliente(User cliente) {
    this.cliente = cliente;
  }

  public List<OrderItem> getItens() {
    return itens;
  }

  public void setItens(List<OrderItem> itens) {
    this.itens = itens;
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

  public Addres getEnderecoEntrega() {
    return enderecoEntrega;
  }

  public void setEnderecoEntrega(Addres enderecoEntrega) {
    this.enderecoEntrega = enderecoEntrega;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  public LocalDateTime getConfimedDate() {
    return confimedDate;
  }

  public void setConfimedDate(LocalDateTime confimedDate) {
    this.confimedDate = confimedDate;
  }

  public LocalDateTime getCancelDate() {
    return cancelDate;
  }

  public void setCancelDate(LocalDateTime cancelDate) {
    this.cancelDate = cancelDate;
  }

  public LocalDateTime getDeliveryDate() {
    return deliveryDate;
  }

  public void setDeliveryDate(LocalDateTime deliveryDate) {
    this.deliveryDate = deliveryDate;
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
    Order other = (Order) obj;
    if (id == null) {
      if (other.id != null) return false;
    } else if (!id.equals(other.id)) return false;
    return true;
  }
}
