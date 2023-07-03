package km1.algafood.api.models;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductModel {
	private Long id;
	private String name;
	private String description;
	private BigDecimal price;
	private Boolean active;
}

