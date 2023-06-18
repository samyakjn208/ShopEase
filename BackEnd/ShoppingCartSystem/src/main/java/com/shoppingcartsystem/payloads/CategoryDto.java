package com.shoppingcartsystem.payloads;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {

	private int categoryId;
	@NotBlank
	private String categoryTitle;
	private String categoryDescription;
	
	
}
