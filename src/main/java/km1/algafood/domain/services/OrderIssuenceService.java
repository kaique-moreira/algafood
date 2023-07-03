package km1.algafood.domain.services;

import jakarta.transaction.Transactional;
import java.util.List;
import km1.algafood.domain.exceptions.DomainException;
import km1.algafood.domain.exceptions.OrderNotFountException;
import km1.algafood.domain.models.Order;
import km1.algafood.domain.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderIssuenceService {

  private final OrderRepository repository;
  private final RestaurantRegisterService restaurantRegisterService;
  private final ProductRegisterService productRegisterService;
  private final CityRegisterService cityRegisterService;
  private final PaymentMethodRegisterService paymentMethodRegisterService;
  private final UserRegisterService userRegisterService;

  @Transactional
  public List<Order> fetchAll() throws DomainException {
    List<Order> cities = repository.findAll();
    return cities;
  }

  @Transactional
  public Order fetchByCode(String code) throws DomainException {
    Order city = repository.findByCode(code).orElseThrow(() -> new OrderNotFountException(code));
    return city;
  }

  @Transactional
  public Order register(Order order) throws DomainException {
    validateOrder(order);
    validateOrderItem(order);

    order.setShippingFee(order.getRestaurant().getShippingFee());
    order.calculateTotal();

    return repository.save(order);
  }

  private void validateOrderItem(Order order) {
    var items = order.getItens();
    items.forEach(
        item -> {
          var product =
              productRegisterService.fetchById(
                  order.getRestaurant().getId(), item.getProduct().getId());

          item.setOrder(order);
          item.setProduct(product);
          item.setUnityPrice(product.getPrice());
        });
  }

  private void validateOrder(Order order) {
    var restaurant = restaurantRegisterService.fetchByID(order.getRestaurant().getId());
    var client = userRegisterService.fetchByID(order.getClient().getId());
    var city = cityRegisterService.fetchByID(order.getDeliveryAddres().getCity().getId());
    var paymentMethod = paymentMethodRegisterService.fetchByID(order.getPaymentMethod().getId());

    order.setRestaurant(restaurant);
    order.setPaymentMethod(paymentMethod);
    order.setClient(client);
    order.getDeliveryAddres().setCity(city);

    if (!restaurant.acceptPaymentMethod(paymentMethod)) {
      throw new DomainException(
          String.format(
              "Payment method %s is not accept by restaurant",
              order.getPaymentMethod().getDescription()));
    }
  }
}
