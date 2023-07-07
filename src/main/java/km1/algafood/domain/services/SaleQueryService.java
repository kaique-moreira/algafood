package km1.algafood.domain.services;

import java.util.List;

import km1.algafood.domain.filter.DailySaleFilter;
import km1.algafood.domain.models.reports.DailySale;

public interface SaleQueryService {

    List<DailySale> queryDailySale(DailySaleFilter filter, String timeOffSet);
  
}
