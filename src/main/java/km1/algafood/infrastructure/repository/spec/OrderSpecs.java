package km1.algafood.infrastructure.repository.spec;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import km1.algafood.domain.models.Order;
import km1.algafood.domain.repositories.filter.OrderFilter;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecs {
  public static Specification<Order> withFilter(OrderFilter filter) {

    return (root, query, builder) -> {
      if (query.getResultType().equals(Order.class)) {
        root.fetch("restaurant").fetch("cuisine");
        root.fetch("client");
      }

      var predicates = new ArrayList<Predicate>();

      if (filter.getClientId() != null) {
        predicates.add(builder.equal(root.get("client").get("id"), filter.getClientId()));
      }

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
      return builder.and(predicates.toArray(new Predicate[0]));
    };
  }
}
