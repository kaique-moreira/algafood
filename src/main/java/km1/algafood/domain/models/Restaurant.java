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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "tb_restaraunt")
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
  private LocalDateTime registerDate;

  @UpdateTimestamp
  @Column(nullable = false, columnDefinition = "timestamp")
  private LocalDateTime updateDate;

  @Embedded private Addres addres;

  @ManyToMany
  @JoinTable(
      name = "restaurant_payment_method",
      joinColumns = @JoinColumn(name = "restaurant_id"),
      inverseJoinColumns = @JoinColumn(name = "payment_method_id"))
  private List<PaymentMethod> paymentMethod = new ArrayList<>();

  @OneToMany(mappedBy = "restaurant")
  private List<Product> produtos = new ArrayList<>();

  public Restaurant() {}

  public Restaurant(
      Long id,
      String name,
      BigDecimal shippingFee,
      Cuisine cozinha,
      Addres addres,
      LocalDateTime registerDate,
      LocalDateTime updateDate,
      List<PaymentMethod> paymentMethod,
      List<Product> produtos) {
    this.id = id;
    this.name = name;
    this.shippingFee = shippingFee;
    this.cuisine = cozinha;
    this.addres = addres;
    this.registerDate = registerDate;
    this.updateDate = updateDate;
    this.paymentMethod = paymentMethod;
    this.produtos = produtos;
  }

  public static RestaurantBuilder builder() {
    return new RestaurantBuilder();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getShippingFee() {
    return shippingFee;
  }

  public void setShippingFee(BigDecimal shippingFee) {
    this.shippingFee = shippingFee;
  }

  public Cuisine getCuisine() {
    return cuisine;
  }

  public void setCuisine(Cuisine cozinha) {
    this.cuisine = cozinha;
  }

  public Addres getAddres() {
    return addres;
  }

  public void setAddres(Addres addres) {
    this.addres = addres;
  }

  public LocalDateTime getRegisterDate() {
    return registerDate;
  }

  public void setRegisterDate(LocalDateTime registerDate) {
    this.registerDate = registerDate;
  }

  public LocalDateTime getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(LocalDateTime updateDate) {
    this.updateDate = updateDate;
  }

  public List<PaymentMethod> getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(List<PaymentMethod> paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  public List<Product> getProdutos() {
    return produtos;
  }

  public void setProdutos(List<Product> produtos) {
    this.produtos = produtos;
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
    Restaurant other = (Restaurant) obj;
    if (id == null) {
      if (other.id != null) return false;
    } else if (!id.equals(other.id)) return false;
    return true;
  }

  public static class RestaurantBuilder {
    private Long id;

    private String name;

    private BigDecimal shippingFee;

    private Cuisine cuisine;

    private Addres addres;

    private LocalDateTime registerDate;

    private LocalDateTime updateDate;

    private List<PaymentMethod> paymentMethod = new ArrayList<>();

    private List<Product> products = new ArrayList<>();

    public RestaurantBuilder id(Long id) {
      this.id = id;
      return this;
    }

    public RestaurantBuilder name(String name) {
      this.name = name;
      return this;
    }

    public RestaurantBuilder shippingFee(BigDecimal shippingFee) {
      this.shippingFee = shippingFee;
      return this;
    }

    public RestaurantBuilder cuisine(Cuisine cuisine) {
      this.cuisine = cuisine;
      return this;
    }

    public RestaurantBuilder addres(Addres addres) {
      this.addres = addres;
      return this;
    }

    public RestaurantBuilder registerDate(LocalDateTime registerDate) {
      this.registerDate = registerDate;
      return this;
    }

    public RestaurantBuilder updateDate(LocalDateTime updateDate) {
      this.updateDate = updateDate;
      return this;
    }

    public RestaurantBuilder paymentMethod(List<PaymentMethod> paymentMethod) {
      this.paymentMethod = paymentMethod;
      return this;
    }

    public RestaurantBuilder products(List<Product> products) {
      this.products = products;
      return this;
    }

    public Restaurant build() {
      return new Restaurant(
          this.id,
          this.name,
          this.shippingFee,
          this.cuisine,
          this.addres,
          this.registerDate,
          this.updateDate,
          this.paymentMethod,
          this.products);
    }
  }
}
