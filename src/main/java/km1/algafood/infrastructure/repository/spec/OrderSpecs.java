package km1.algafood.infrastructure.repository.spec;

import java.util.ArrayList;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import km1.algafood.domain.filter.OrderFilter;
import km1.algafood.domain.models.Order;

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
