package km1.algafood.domain.models;

import java.util.ArrayList;
import java.util.List;


public class Cuisine {

  private Long id;
  private String name;
  private List<Restaurant> restaurants = new ArrayList<>();

  public Cuisine() {
  }

  public Cuisine(Long id, String name, List<Restaurant> restaurantes) {
    this.id = id;
    this.name = name;
    this.restaurants = restaurantes;
  }

  public Cuisine(String name, List<Restaurant> restaurantes) {
    this.name = name;
    this.restaurants = restaurantes;
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

  public List<Restaurant> getRestaurants() {
    return restaurants;
  }

  public void setRestaurants(List<Restaurant> restaurants) {
    this.restaurants = restaurants;
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
    Cuisine other = (Cuisine) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  public static CuisineBuilder builder(){
    return new CuisineBuilder();
  }
  public static class CuisineBuilder {
    private Long id;
    private String name;
    private List<Restaurant> restaurants = new ArrayList<>();

    public CuisineBuilder id(Long id) {
      this.id = id;
      return this;
    };

    public CuisineBuilder name(String name) {
      this.name = name;
      return this;
    };

    public CuisineBuilder restaurants(List<Restaurant> restaurants) {
      this.restaurants = restaurants;
      return this;
    };

    public Cuisine build(){
        return new Cuisine(this.id, this.name, this.restaurants);
    }
  }
}
