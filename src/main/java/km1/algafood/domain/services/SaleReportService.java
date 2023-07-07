package km1.algafood.domain.services;

import km1.algafood.domain.filter.DailySaleFilter;

public interface SaleReportService {
  byte[] issueDailySaleReport(DailySaleFilter filter, String timeOffSet);
  
}
