package km1.algafood.domain.models.reports;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DailySale {
  @JsonFormat(pattern = "dd-MM-yyyy")
  private Date date;
  private Long totalSales;
  private BigDecimal totalBilled;
}
