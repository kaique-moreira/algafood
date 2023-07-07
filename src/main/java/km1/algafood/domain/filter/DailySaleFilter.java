package km1.algafood.domain.filter;

import java.time.OffsetDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DailySaleFilter {
  private Long restaurantId;
  @DateTimeFormat(iso = ISO.DATE_TIME)
  private OffsetDateTime createdDateStart;
  @DateTimeFormat(iso = ISO.DATE_TIME)
  private OffsetDateTime createdDateEnd;
}
