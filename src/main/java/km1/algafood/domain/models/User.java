package km1.algafood.domain.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {
	private Long id;

	private String name;
	
	private String email;
	
	private String password;
	
	private LocalDateTime registerDate;
	
	private List<Group> grupos = new ArrayList<>();

  public User() {
  }

  public User(Long id, String name, String email, String password, LocalDateTime registerDate, List<Group> grupos) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.registerDate = registerDate;
    this.grupos = grupos;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public LocalDateTime getRegisterDate() {
    return registerDate;
  }

  public void setRegisterDate(LocalDateTime registerDate) {
    this.registerDate = registerDate;
  }

  public List<Group> getGrupos() {
    return grupos;
  }

  public void setGrupos(List<Group> grupos) {
    this.grupos = grupos;
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
    User other = (User) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

}
