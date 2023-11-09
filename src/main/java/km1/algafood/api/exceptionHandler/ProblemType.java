package km1.algafood.api.exceptionHandler;

import lombok.Getter;

@Getter
public enum ProblemType {
  RESOURCE_NOT_FOUND("/resource-not-found", "Resource not found"),
  RESOURCE_HAS_DEPENDENTS("/resource-has-dependents", "Resource has dependents"),
  DOMAIN_ERROR("/domain-error", "violation of a domain rule");

  private String title;
  private String uri;

  ProblemType(String path, String title) {
    this.uri = "https://algafood.com.br" + path;
    this.title = title;
  }
}
