package km1.algafood.api.controllers;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import km1.algafood.api.assemblers.OrderInputDisassembler;
import km1.algafood.api.assemblers.OrderModelAssembler;
import km1.algafood.api.assemblers.OrderSummaryModelAssembler;
import km1.algafood.api.models.OrderModel;
import km1.algafood.api.models.input.OrderInput;
import km1.algafood.domain.models.User;
import km1.algafood.domain.services.OrderIssuenceService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrderController {
  private final OrderIssuenceService registerService;
  private final OrderModelAssembler assembler;
  private final OrderSummaryModelAssembler summaryAssembler;
  private final OrderInputDisassembler disassembler;

  @PostMapping
  public OrderModel save(@RequestBody OrderInput orderInput) {
    var order = disassembler.toDomainObject(orderInput);
    order.setClient(User.builder().id(1l).build());
    return assembler.toModel(registerService.register(order));
  }

  @GetMapping
  public MappingJacksonValue list(@RequestParam(required = false) String filds) {
    var orders = registerService.fetchAll();
    var ordersModel = summaryAssembler.toCollectionModel(orders);

    MappingJacksonValue ordersWrapper = new MappingJacksonValue(ordersModel);

    SimpleFilterProvider filterProvider = new SimpleFilterProvider();
    filterProvider.addFilter("orderFilter", SimpleBeanPropertyFilter.serializeAll());

    if (StringUtils.isNotBlank(filds)) {
      filterProvider.addFilter(
          "orderFilter", SimpleBeanPropertyFilter.filterOutAllExcept(filds.split(",")));
    }

    ordersWrapper.setFilters(filterProvider);

    return ordersWrapper;
  }

  @GetMapping("/{orderCode}")
  public OrderModel fetch(@PathVariable String orderCode) {
    return assembler.toModel(registerService.fetchByCode(orderCode));
  }
}
