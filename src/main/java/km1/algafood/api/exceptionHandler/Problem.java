package km1.algafood.api.exceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Problem{
    private String type;
    private String title;
    private Integer status;
    private String detail;
    private String instance;
    private OffsetDateTime timestamp;
    private Map<String, Object> properties;
    private List<Object> objects;

  @Getter
	@Builder
	public static class Object {
		private String name;
		private String userMessage;
	} 
}
