package km1.algafood.api.controllers;

import java.util.List;
import km1.algafood.domain.filter.DailySaleFilter;
import km1.algafood.domain.models.reports.DailySale;
import km1.algafood.domain.services.SaleQueryService;
import km1.algafood.domain.services.SaleReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/reports")
@AllArgsConstructor
public class ReportController {
  private final SaleQueryService queryService;
  private final SaleReportService saleReportService;

  @GetMapping(path = "/daily-sales", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<DailySale> dailySaleReport(
      DailySaleFilter filter,
      @RequestParam(required = false, defaultValue = "+00:00") String timeOffSet) {
    return queryService.queryDailySale(filter, timeOffSet);
  }

  @GetMapping(path = "/daily-sales", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> dailySalePDFReport(
      DailySaleFilter filter,
      @RequestParam(required = false, defaultValue = "+00:00") String timeOffSet) {
    var headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=daily-sales.pdf");
    return ResponseEntity.ok()
        .headers(headers)
        .body(saleReportService.issueDailySaleReport(filter, timeOffSet));
  }
}
