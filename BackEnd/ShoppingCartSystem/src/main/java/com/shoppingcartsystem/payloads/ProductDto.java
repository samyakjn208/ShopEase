package com.shoppingcartsystem.payloads;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ProductDto {

	private int productId;
	private String productName;
	private String productDescription;
	private List<String> productPhoto;
	private double productMRP;
	private double productPrice;
	private Integer stock;
	private Integer merchantId;
	private CategoryDto category;
}
