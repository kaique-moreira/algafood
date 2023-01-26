package km1.algafood.api.exceptionHandler;

import java.time.OffsetDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import km1.algafood.api.exceptionHandler.Problem.ProblemBuilder;
import km1.algafood.domain.exceptions.DomainException;
import km1.algafood.domain.exceptions.EntityHasDependents;
import km1.algafood.domain.exceptions.EntityNotFoundException;

@EnableWebMvc
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  public static final String DEFAULT_ERROR_MESSAGE = "An unexpected internal system error has occurred. Try again and if"
      + "the problem persists, contact your system administrator.";

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex, Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    if (body == null) {
      body = Problem.builder()
          .timestamp(OffsetDateTime.now())
          .title(((HttpStatus) status).getReasonPhrase())
          .status(status)
          .detail(DEFAULT_ERROR_MESSAGE)
          .build();
    } else if (body instanceof String) {
      body = Problem.builder()
          .timestamp(OffsetDateTime.now())
          .title((String) body)
          .status(status)
          .detail(DEFAULT_ERROR_MESSAGE)
          .build();
    }

    return super.handleExceptionInternal(ex, body, headers, status, request);
  }

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<?> handleDomainException(DomainException ex, WebRequest request) {

    HttpStatus status = HttpStatus.BAD_REQUEST;
    ProblemType problemType = ProblemType.DOMAIN_ERROR;
    String detail = ex.getMessage();

    Problem problem = createProblemBuilder(status, problemType, detail)
        .build();

    return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
    HttpStatus status = HttpStatus.NOT_FOUND;
    ProblemType problemType = ProblemType.RESOURCE_NOT_FOUND;
    String detail = ex.getMessage();

    Problem problem = createProblemBuilder(status, problemType, detail).instance(request.getContextPath()).build();
    System.out.println(problem);

    return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
  }

  @ExceptionHandler(EntityHasDependents.class)
  public ResponseEntity<?> handleEntityHasDependents(EntityHasDependents ex, WebRequest request) {

    HttpStatus status = HttpStatus.CONFLICT;
    ProblemType problemType = ProblemType.RESOURCE_HAS_DEPENDENTS;
    String detail = ex.getMessage();

    Problem problem = createProblemBuilder(status, problemType, detail)
        .build();

    return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
  }

  private ProblemBuilder createProblemBuilder(
      HttpStatus status, ProblemType problemType, String detail) {
    return Problem.builder()
        .timestamp(OffsetDateTime.now())
        .status(status)
        .type(problemType.getUri())
        .title(problemType.getTitle())
        .detail(detail);
  }
}
