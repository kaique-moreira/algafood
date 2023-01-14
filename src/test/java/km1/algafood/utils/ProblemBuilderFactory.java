package km1.algafood.utils;

import java.time.OffsetDateTime;
import org.springframework.http.HttpStatusCode;

import km1.algafood.api.exceptionHandler.Problem;

public class ProblemBuilderFactory {

  public static Problem.ProblemBuilder notFoundProblem() {
    return Problem.builder()
        .title("Resource not found")
        .type("https://algafood.com.br/resource-not-found")
        .status(HttpStatusCode.valueOf(404))
        .detail("")
        .instance("").timestamp(OffsetDateTime.now()).properties(null);
  }

  public static Problem.ProblemBuilder conflictProblem() {
    return Problem.builder()
        .title("Resource has dependents")
        .type("https://algafood.com.br/resource-has-dependents")
        .status(HttpStatusCode.valueOf(409))
        .detail("")
        .instance("").timestamp(OffsetDateTime.now()).properties(null);
  }
}
