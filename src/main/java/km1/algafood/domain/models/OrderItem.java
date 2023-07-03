package km1.algafood.domain.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_order_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class OrderItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
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

  public void calcTotalPrice() {
    var unityPrice = this.unityPrice;
    var quantity = this.quantity;

    if (unityPrice == null) {
      unityPrice = BigDecimal.ZERO;
    }
    if (quantity == null) {
       quantity = 0;
    }

    setTotalPrice(getUnityPrice().multiply(BigDecimal.valueOf(getQuantity())));
  }
}
