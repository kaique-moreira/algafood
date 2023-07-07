package km1.algafood.infrastructure.service.report;

import java.util.HashMap;
import java.util.Locale;

import org.springframework.stereotype.Service;

import km1.algafood.domain.filter.DailySaleFilter;
import km1.algafood.domain.services.SaleQueryService;
import km1.algafood.domain.services.SaleReportService;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@AllArgsConstructor
public class SaleReportPDfService implements SaleReportService {
  private final SaleQueryService saleQueryService;
  @Override
  public byte[] issueDailySaleReport(DailySaleFilter filter, String timeOffSet) {
    try {
    var inputStream = this.getClass().getResourceAsStream("/reports/daily-sales.jasper");

    var params =  new HashMap<String,Object>();

    params.put("REPORT_LOCALE", new Locale("pt", "br"));

    var datasource = new JRBeanCollectionDataSource(saleQueryService.queryDailySale(filter, timeOffSet));
    var jasperPrint = JasperFillManager.fillReport(inputStream, params, datasource);
    return JasperExportManager.exportReportToPdf(jasperPrint);
    } catch (JRException e) {
      throw new ReportException("", e);
    }
  }
  
}
