package km1.algafood.domain.models;

public class Addres {
  private String postalCode;

  private String streat;

  private String number;

  private String complement;

  private String district;

  private City city;


  public Addres() {
  }

  public Addres(String postalCode, String streat, String number, String complement, String district, City city) {
    this.postalCode = postalCode;
    this.streat = streat;
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

  public String getStreat() {
    return streat;
  }

  public void setStreat(String streat) {
    this.streat = streat;
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
