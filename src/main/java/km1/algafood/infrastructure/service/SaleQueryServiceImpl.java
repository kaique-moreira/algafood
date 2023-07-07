package km1.algafood.infrastructure.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;
import km1.algafood.domain.filter.DailySaleFilter;
import km1.algafood.domain.models.Order;
import km1.algafood.domain.models.OrderStatus;
import km1.algafood.domain.models.reports.DailySale;
import km1.algafood.domain.services.SaleQueryService;

@Repository
public class SaleQueryServiceImpl implements SaleQueryService {
  @PersistenceContext
  private EntityManager manager;

  @Override
  public List<DailySale> queryDailySale(DailySaleFilter filter, String timeOffSet) {
    var builder = manager.getCriteriaBuilder();
    var query = builder.createQuery(DailySale.class);
    var root = query.from(Order.class);

    var funcTimezoneCreatedDate =
        builder.function(
            "timezone", Date.class, builder.literal(timeOffSet), root.get("createdDate"));
    var funcDateCreatedDate = builder.function("date", Date.class, funcTimezoneCreatedDate);

    var selection =
        builder.construct(
            DailySale.class,
            funcDateCreatedDate,
            builder.count(root.get("id")),
            builder.sum(root.get("total")));

    var predicates = new ArrayList<Predicate>();

    if (filter.getRestaurantId() != null) {
      predicates.add(builder.equal(root.get("restaurant").get("id"), filter.getRestaurantId()));
    }

    if (filter.getCreatedDateStart() != null) {
      predicates.add(
          builder.greaterThanOrEqualTo(root.get("createdDate"), filter.getCreatedDateStart()));
    }

    if (filter.getCreatedDateEnd() != null) {
      predicates.add(
          builder.lessThanOrEqualTo(root.get("createdDate"), filter.getCreatedDateEnd()));
    }
    predicates.add(root.get("status").in(OrderStatus.CONFIRMED, OrderStatus.DELIVERED));
    
    var restrictions = predicates.toArray(new Predicate[0]);

    query.select(selection);
    query.where(restrictions);
    query.groupBy(funcDateCreatedDate);

    return manager.createQuery(query).getResultList();
  }
}
