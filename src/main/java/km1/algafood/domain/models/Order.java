package km1.algafood.domain.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import km1.algafood.domain.exceptions.DomainException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "tb_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;
  private String code;
  private BigDecimal subtotal;
  private BigDecimal shippingFee;
  private BigDecimal total;

  @Embedded private Addres deliveryAddres;

  @Enumerated(EnumType.STRING)
  @Builder.Default
  private OrderStatus status = OrderStatus.CREATED;

  @CreationTimestamp private OffsetDateTime createdDate;

  private OffsetDateTime confirmDate;
  private OffsetDateTime cancelDate;
  private OffsetDateTime deliveryDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private PaymentMethod paymentMethod;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Restaurant restaurant;

  @ManyToOne
  @JoinColumn(name = "user_client_id", nullable = false)
  private User client;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  @Builder.Default
  private List<OrderItem> itens = new ArrayList<>();

  public void confirm() {
    if (!getStatus().equals(OrderStatus.CONFIRMED)) {
      setStatus(OrderStatus.CONFIRMED);
      setConfirmDate(OffsetDateTime.now());
    }
  }

  public void cancel() {
    if (!getStatus().equals(OrderStatus.CANCELED)) {
      setStatus(OrderStatus.CANCELED);
      setCancelDate(OffsetDateTime.now());
    }
  }

  public void delivery() {
    if (!getStatus().equals(OrderStatus.DELIVERED)) {
      setStatus(OrderStatus.DELIVERED);
      setDeliveryDate(OffsetDateTime.now());
    }
  }

  private void setStatus(OrderStatus status) {
    if (getStatus().cannotChange(status)) {
      throw new DomainException(
          String.format(
              "Status do pedido %s não pode ser alterado de  %s para %s",
              getCode(), getStatus().getDescription(), status));
    }
    this.status = status;
  }

  public void calculateTotal() {

    this.subtotal =
        getItens().stream()
            .map(
                item -> {
                  item.calcTotalPrice();
                  return item.getTotalPrice();
                })
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    this.total = subtotal.add(shippingFee);
  }

  @PrePersist
  private void generateCode(){
    setCode(UUID.randomUUID().toString());
  }
}
