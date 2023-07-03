package km1.algafood.api.controllers;

import java.util.Collection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import km1.algafood.api.assemblers.OrderInputDisassembler;
import km1.algafood.api.assemblers.OrderModelAssembler;
import km1.algafood.api.assemblers.OrderSummaryModelAssembler;
import km1.algafood.api.models.OrderModel;
import km1.algafood.api.models.OrderSummaryModel;
import km1.algafood.api.models.input.OrderInput;
import km1.algafood.domain.models.User;
import km1.algafood.domain.services.OrderIssuenceService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrderController {
  private final OrderIssuenceService registerService;  
  private final OrderModelAssembler assembler;
  private final OrderSummaryModelAssembler summaryAssembler;
  private final OrderInputDisassembler disassembler;

  @PostMapping
  public OrderModel save(@RequestBody OrderInput orderInput){
    var order = disassembler.toDomainObject(orderInput);
    order.setClient(User.builder().id(1l).build());
    return assembler.toModel(registerService.register(order));
  }
  @GetMapping
  public Collection<OrderSummaryModel> list(){
    return summaryAssembler.toCollectionModel(registerService.fetchAll());
  }

  @GetMapping("/{orderCode}")
  public OrderModel list(@PathVariable String orderCode){
    return assembler.toModel(registerService.fetchByCode(orderCode));
  }
}

