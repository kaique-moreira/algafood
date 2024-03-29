package km1.algafood.api.controllers;

import java.util.Map;
import km1.algafood.api.assemblers.OrderInputDisassembler;
import km1.algafood.api.assemblers.OrderModelAssembler;
import km1.algafood.api.assemblers.OrderSummaryModelAssembler;
import km1.algafood.api.models.OrderModel;
import km1.algafood.api.models.OrderSummaryModel;
import km1.algafood.api.models.input.OrderInput;
import km1.algafood.core.data.PageableTranslator;
import km1.algafood.domain.filter.OrderFilter;
import km1.algafood.domain.models.User;
import km1.algafood.domain.repositories.OrderRepository;
import km1.algafood.domain.services.OrderIssuenceService;
import km1.algafood.infrastructure.repository.spec.OrderSpecs;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrderController {
  private final OrderIssuenceService registerService;
  private final OrderModelAssembler assembler;
  private final OrderRepository repository;
  private final OrderSummaryModelAssembler summaryAssembler;
  private final OrderInputDisassembler disassembler;

  @PostMapping
  public OrderModel save(@RequestBody OrderInput orderInput) {
    var order = disassembler.toDomainObject(orderInput);
    order.setClient(User.builder().id(1l).build());
    return assembler.toModel(registerService.register(order));
  }

  @GetMapping
  public Page<OrderSummaryModel> list(Pageable pageable, OrderFilter filter) {
    pageable = translatePageable(pageable);
    var ordersPage = repository.findAll(OrderSpecs.withFilter(filter), pageable);
    var ordersModel = summaryAssembler.toCollectionModel(ordersPage.getContent());
    var ordersModelPage = new PageImpl<OrderSummaryModel>(ordersModel);
    return ordersModelPage;
  }

  // @GetMapping
  // public MappingJacksonValue list(@RequestParam(required = false) String filds) {
  //   var orders = registerService.fetchAll();
  //   var ordersModel = summaryAssembler.toCollectionModel(orders);

  //   MappingJacksonValue ordersWrapper = new MappingJacksonValue(ordersModel);

  //   SimpleFilterProvider filterProvider = new SimpleFilterProvider();
  //   filterProvider.addFilter("orderFilter", SimpleBeanPropertyFilter.serializeAll());

  //   if (StringUtils.isNotBlank(filds)) {
  //     filterProvider.addFilter(
  //         "orderFilter", SimpleBeanPropertyFilter.filterOutAllExcept(filds.split(",")));
  //   }

  //   ordersWrapper.setFilters(filterProvider);

  //   return ordersWrapper;
  // }

  @GetMapping("/{orderCode}")
  public OrderModel fetch(@PathVariable String orderCode) {
    return assembler.toModel(registerService.fetchByCode(orderCode));
  }

  private Pageable translatePageable(Pageable pageable) {
    var map =
        Map.of(
            "clientName", "client.name",
            "restaurant.name", "restaurant.name",
            "total", "total",
            "code", "code");
    return PageableTranslator.translate(pageable, map);
  }
}
