package km1.algafood.api.exceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import km1.algafood.api.exceptionHandler.Problem.ProblemBuilder;
import km1.algafood.domain.exceptions.DomainException;
import km1.algafood.domain.exceptions.EntityHasDependents;
import km1.algafood.domain.exceptions.EntityNotFoundException;
import lombok.Setter;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@EnableWebMvc
@RestControllerAdvice
@Setter
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  public static final String DEFAULT_ERROR_MESSAGE =
      "An unexpected internal system error has occurred. Try again and if"
          + "the problem persists, contact your system administrator.";

  @Autowired private MessageSource messageSource;

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    ProblemType problemType = ProblemType.INVALID_FILDS;
    String detail = "";
    BindingResult bidingResult = ex.getBindingResult();
    List<Problem.Object> problemObjects =
        bidingResult.getAllErrors().stream()
            .map(this::extracted)
            .toList();

    Problem problem =
        createProblemBuilder((HttpStatus) status, problemType, detail)
            .detail(detail)
            .type(problemType.getUri())
            .title(problemType.getTitle())
            .objects(problemObjects)
            .build();

    return handleExceptionInternal(ex, problem, headers, status, request);
  }

  private Problem.Object extracted(ObjectError objectError) {
    String message =
        messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

    String name = objectError.getObjectName();

    if (objectError instanceof FieldError) {
      name = ((FieldError) objectError).getField();
    }

    return Problem.Object.builder().name(name).userMessage(message).build();
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    ProblemType problemType = ProblemType.SYSTEM_ERROR;
    String detail = DEFAULT_ERROR_MESSAGE;

    ex.printStackTrace();

    Problem problem = createProblemBuilder(status, problemType, detail).detail(detail).build();

    return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
  }

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
      NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    ProblemType problemType = ProblemType.RESOURCE_NOT_FOUND;
    String detail =
        String.format("O recurso %s, que você tentou acessar, é inexistente.", ex.getRequestURL());

    Problem problem =
        createProblemBuilder((HttpStatus) status, problemType, detail)
            .detail(DEFAULT_ERROR_MESSAGE)
            .build();

    return handleExceptionInternal(ex, problem, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(
      TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    if (ex instanceof MethodArgumentTypeMismatchException) {
      return handleMethodArgumentTypeMismatch(
          (MethodArgumentTypeMismatchException) ex, headers, (HttpStatus) status, request);
    }
    return super.handleTypeMismatch(ex, headers, status, request);
  }

  private ResponseEntity<Object> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    ProblemType problemType = ProblemType.INVALID_PARAMS;

    String detail =
        String.format(
            "O parâmetro de URL '%s' recebeu o valor '%s', "
                + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
            ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

    Problem problem =
        createProblemBuilder(status, problemType, detail).detail(DEFAULT_ERROR_MESSAGE).build();

    return handleExceptionInternal(ex, problem, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex, Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    if (body == null) {
      body =
          Problem.builder()
              .timestamp(OffsetDateTime.now())
              .title(((HttpStatus) status).getReasonPhrase())
              .status(status.value())
              .detail(DEFAULT_ERROR_MESSAGE)
              .build();
    } else if (body instanceof String) {
      body =
          Problem.builder()
              .timestamp(OffsetDateTime.now())
              .title((String) body)
              .status(status.value())
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

    Problem problem = createProblemBuilder(status, problemType, detail).build();

    return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex, WebRequest request) {
    HttpStatus status = HttpStatus.NOT_FOUND;
    ProblemType problemType = ProblemType.RESOURCE_NOT_FOUND;
    String detail = ex.getMessage();

    Problem problem =
        createProblemBuilder(status, problemType, detail)
            .instance(request.getContextPath())
            .build();

    return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
  }

  @ExceptionHandler(EntityHasDependents.class)
  public ResponseEntity<?> handleEntityHasDependents(EntityHasDependents ex, WebRequest request) {

    HttpStatus status = HttpStatus.CONFLICT;
    ProblemType problemType = ProblemType.RESOURCE_HAS_DEPENDENTS;
    String detail = ex.getMessage();

    Problem problem = createProblemBuilder(status, problemType, detail).build();

    return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    Throwable rootCuase = ExceptionUtils.getRootCause(ex);

    if (rootCuase instanceof InvalidFormatException) {
      return handleInvalidFormatException(
          (InvalidFormatException) rootCuase, headers, status, request);
    }

    ProblemType problemType = ProblemType.DOMAIN_ERROR;
    String detail = "O corpo da requisição está invalido, Verique e tente novamente";
    Problem problem = createProblemBuilder((HttpStatus) status, problemType, detail).build();
    return handleExceptionInternal(ex, problem, headers, status, request);
  }

  private ResponseEntity<Object> handleInvalidFormatException(
      InvalidFormatException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

    String path =
        ex.getPath().stream().map(ref -> ref.getFieldName()).collect(Collectors.joining("."));

    ProblemType problemType = ProblemType.MESSAGE_NOT_READABLE;
    String detail =
        String.format(
            "A propriedade '%s' recebeu o valor '%s', "
                + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
            path, ex.getValue(), ex.getTargetType().getSimpleName());

    Problem problem = createProblemBuilder((HttpStatus) status, problemType, detail).build();

    return handleExceptionInternal(ex, problem, headers, status, request);
  }

  private ProblemBuilder createProblemBuilder(
      HttpStatus status, ProblemType problemType, String detail) {
    return Problem.builder()
        .timestamp(OffsetDateTime.now())
        .status(status.value())
        .type(problemType.getUri())
        .title(problemType.getTitle())
        .detail(detail);
  }
}
