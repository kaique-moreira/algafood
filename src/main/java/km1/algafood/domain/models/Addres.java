package km1.algafood.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;

@Embeddable
@Builder
public class Addres {

  @Column(name = "addres_postal_code")
  private String postalCode;

  @Column(name = "addres_street")
  private String street;

  @Column(name = "addres_number")
  private String number;

  @Column(name = "addres_complement")
  private String complement;

  @Column(name = "addres_district")
  private String district;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "addres_city_id")
  private City city;


  public Addres() {
  }

  public Addres(String postalCode, String streat, String number, String complement, String district, City city) {
    this.postalCode = postalCode;
    this.street = streat;
    this.number = number;
    this.complement = complement;
    this.district = district;
    this.city = city;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String streat) {
    this.street = streat;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getComplement() {
    return complement;
  }

  public void setComplement(String complement) {
    this.complement = complement;
  }

  public String getDistrict() {
    return district;
  }

  public void setDistrict(String district) {
    this.district = district;
  }

  public City getCity() {
    return city;
  }

  public void setCity(City city) {
    this.city = city;
  }

}
