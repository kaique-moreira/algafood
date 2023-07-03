package km1.algafood.api.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import km1.algafood.api.assemblers.ProductInputDisassembler;
import km1.algafood.api.assemblers.ProductModelAssembler;
import km1.algafood.api.models.ProductInput;
import km1.algafood.api.models.ProductModel;
import km1.algafood.domain.services.ProductRegisterService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/restaurants/{restaurantId}/products")
@AllArgsConstructor
public class RestaurantProductController {

  private final ProductRegisterService registerService;
  private final ProductModelAssembler assembler;
  private final ProductInputDisassembler disassembler;

  @PostMapping
  public ProductModel add(@PathVariable Long restaurantId, @RequestBody ProductInput productInput) {
    var toRegister = disassembler.toDomainObject(productInput);
    var registered = registerService.register(restaurantId, toRegister);
    var model = assembler.toModel(registered);
    return model;
  }

  @GetMapping
  public List<ProductModel> list(@PathVariable Long restaurantId) {
    return (List<ProductModel>)
        assembler.toCollectionModel(registerService.fetchByRestaurantId(restaurantId));
  }

  @GetMapping("/{productId}")
  public ProductModel fetch(@PathVariable Long restaurantId, @PathVariable Long productId) {
    return assembler.toModel(registerService.fetchById(restaurantId, productId));
  }

 @PutMapping("/{produtoId}")
    public ProductModel atualizar(@PathVariable Long restaurantId, @PathVariable Long productId,
            @RequestBody @Valid ProductInput produtoInput) {
    var toUpdate = disassembler.toDomainObject(produtoInput);
    var updated = registerService.update(restaurantId,productId,toUpdate);
    var model = assembler.toModel(updated);
    return model;
    }  
}
