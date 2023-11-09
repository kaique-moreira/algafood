package km1.algafood.domain.models;

import java.math.BigDecimal;

public class Product {
	private Long id;

	private String name;
	
	private String description;
	
	private BigDecimal price;
	
	private Boolean active;

	private Restaurant restaurant;

  public Product() {
  }

  public Product(Long id, String nome, String descricao, BigDecimal preco, Boolean ativo, Restaurant restaurant) {
    this.id = id;
    this.name = nome;
    this.description = descricao;
    this.price = preco;
    this.active = ativo;
    this.restaurant = restaurant;
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

  public void setName(String nome) {
    this.name = nome;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String descricao) {
    this.description = descricao;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal preco) {
    this.price = preco;
  }

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean ativo) {
    this.active = ativo;
  }

  public Restaurant getRestaurant() {
    return restaurant;
  }

  public void setRestaurant(Restaurant restaurant) {
    this.restaurant = restaurant;
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
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Product other = (Product) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

}

