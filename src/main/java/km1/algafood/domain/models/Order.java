package km1.algafood.domain.models;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

  private BigDecimal subtotal;
  private BigDecimal shippingFee;
  private BigDecimal total;

  @Embedded private Addres enderecoEntrega;

  private OrderStatus status;
  @CreationTimestamp private OffsetDateTime createdDate;
  private OffsetDateTime confimedDate;
  private OffsetDateTime cancelDate;
  private OffsetDateTime deliveryDate;

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
  @Builder.Default
  private List<OrderItem> itens = new ArrayList<>();
}
