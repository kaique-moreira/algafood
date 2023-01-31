package km1.algafood.api.exceptionHandler;

public enum ProblemType {
  RESOURCE_NOT_FOUND("/resource-not-found", "Resource not found"),
  RESOURCE_HAS_DEPENDENTS("/resource-has-dependents", "Resource has dependents"),
  DOMAIN_ERROR("/domain-error", "violation of a domain rule"),
  INVALID_FILDS("", ""),
  MESSAGE_NOT_READABLE("", ""),
  SYSTEM_ERROR("", ""),
  INVALID_PARAMS("", "");

  private String title;
  private String uri;

  ProblemType(String path, String title) {
    this.uri = "https://algafood.com.br" + path;
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public String getUri() {
    return uri;
  }
}
