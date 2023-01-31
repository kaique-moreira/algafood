package km1.algafood.api.exceptionHandler;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

import lombok.Builder;
import lombok.Getter;

public class Problem extends ProblemDetail {
  private OffsetDateTime timestamp;
  private List<Object> objects;
  
  public Problem() {
  }

  public Problem(ProblemDetail other, OffsetDateTime timestamp, List<Object> objects) {
    super(other);
    this.timestamp = timestamp;
    this.objects = objects;
  }

  public static ProblemBuilder builder() {
    return new ProblemBuilder();
  }

  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public List<Object> getObjects() {
    return objects;
  }

  public void setObjects(List<Object> objects) {
    this.objects = objects;
  }

  @Getter
	@Builder
	public static class Object {
		private String name;
		private String userMessage;
	}
  public static class ProblemBuilder {

    private String type;
    private String title;
    private HttpStatusCode status;
    private String detail;
    private String instance;
    private OffsetDateTime timestamp;
    private Map<String, Object> properties;
    private List<Object> objects;

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

    public ProblemBuilder objetcs(List<Object> objects) {
      this.objects = objects;
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

    public ProblemBuilder timestamp(OffsetDateTime timestamp) {
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
      return new Problem(problemDetail, this.timestamp, this.objects);
    }
  }
}
