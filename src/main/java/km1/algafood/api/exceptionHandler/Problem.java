package km1.algafood.api.exceptionHandler;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

public class Problem extends ProblemDetail {
  private LocalDateTime timestamp;

  public Problem() {
  }

  public Problem(ProblemDetail other, LocalDateTime timestamp) {
    super(other);
    this.timestamp = timestamp;
  }

  public static ProblemBuilder builder() {
    return new ProblemBuilder();
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public static class ProblemBuilder {

    private String type;
    private String title;
    private HttpStatusCode status;
    private String detail;
    private String instance;
    private LocalDateTime timestamp;
    private Map<String, Object> properties;

    public ProblemBuilder type(String type) {
      this.type = type;
      return this;
    }

    public ProblemBuilder() {
    }

    public ProblemBuilder title(String title) {
      this.title = title;
      return this;
    }

    public ProblemBuilder status(HttpStatusCode status) {
      this.status = status;
      return this;
    }

    public ProblemBuilder detail(String detail) {
      this.detail = detail;
      return this;
    }

    public ProblemBuilder instance(String instance) {
      this.instance = instance;
      return this;
    }

    public ProblemBuilder properties(Map<String, Object> properties) {
      this.properties = properties;
      return this;
    }

    public ProblemBuilder timestamp(LocalDateTime timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public Problem build() {
      ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(this.status, this.detail);
      if (this.type != null) {
        problemDetail.setType(URI.create(this.type));
      }
      problemDetail.setTitle(this.title);
      if (this.instance != null) {
        problemDetail.setInstance(URI.create(this.instance));
      }
      if (this.properties != null) {
        this.properties.forEach((k, v) -> problemDetail.setProperty(k, v));
      }
      return new Problem(problemDetail, this.timestamp);
    }
  }
}
