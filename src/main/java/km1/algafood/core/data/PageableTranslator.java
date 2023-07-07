package km1.algafood.core.data;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import km1.algafood.domain.exceptions.DomainException;

public class PageableTranslator {
  public static Pageable translate(Pageable pageable, Map<String, String> fieldsMap) {
    var orders =
        pageable.getSort().stream()
            .map(order -> newOrder(order, fieldsMap))
            .collect(Collectors.toList());
    return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(orders));
  }

  public static Sort.Order newOrder(Sort.Order order, Map<String, String> fildsMap) {
    var property = order.getProperty();
    if (!fildsMap.containsKey(property)) {
      throw new DomainException(String.format("Property %s is invalid to sort", property));
    }
    return new Sort.Order(order.getDirection(), fildsMap.get(order.getProperty()));
  }
}
