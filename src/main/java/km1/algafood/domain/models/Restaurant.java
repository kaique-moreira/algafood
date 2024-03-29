package km1.algafood.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "tb_restaurant")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Restaurant {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private BigDecimal shippingFee;

  @ManyToOne
  @JoinColumn(name = "cuisine_id", nullable = false)
  private Cuisine cuisine;

  @Column(nullable = false, columnDefinition = "timestamp")
  @CreationTimestamp
  private OffsetDateTime registerDate;

  @UpdateTimestamp
  @Column(columnDefinition = "timestamp")
  private OffsetDateTime updateDate;

  @Embedded private Addres addres;

  @ManyToMany
  @JoinTable(
      name = "restaurant_payment_method",
      joinColumns = @JoinColumn(name = "restaurant_id"),
      inverseJoinColumns = @JoinColumn(name = "payment_method_id"))
  @Builder.Default
  private Set<PaymentMethod> paymentMethod = new HashSet();

  @ManyToMany
  @JoinTable(
      name = "restaurant_user",
      joinColumns = @JoinColumn(name = "restaurant_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id"))
  @Builder.Default
  private Set<User> members = new HashSet();

  @OneToMany(mappedBy = "restaurant")
  @Builder.Default
  private List<Product> products = new ArrayList<>();

  @Builder.Default private Boolean active = Boolean.TRUE;

  @Builder.Default private Boolean opened = Boolean.TRUE;

  public boolean addPaymentMethod(PaymentMethod paymentMethod) {
    return this.getPaymentMethod().add(paymentMethod);
  }

  public boolean removePaymentMethod(PaymentMethod paymentMethod) {
    return this.getPaymentMethod().remove(paymentMethod);
  }

  public boolean addProduct(Product toAdd) {
    return this.getProducts().add(toAdd);
  }

  public boolean removeProduct(Product toRemove) {
    return this.getProducts().remove(toRemove);
  }

  public void active() {
    setActive(true);
  }

  public void disactive() {
    setActive(false);
  }

  public void open() {
    setOpened(true);
  }

  public void close() {
    setOpened(false);
  }

  public boolean removeMember(User toRemove) {
    return this.getMembers().remove(toRemove);
  }

  public boolean addMember(User toAdd) {
    return this.getMembers().add(toAdd);
  }

  public boolean acceptPaymentMethod(PaymentMethod paymentMethod) {
    return this.paymentMethod.contains(paymentMethod);
  }
}
