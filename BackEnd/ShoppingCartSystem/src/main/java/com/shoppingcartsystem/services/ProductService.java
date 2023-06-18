package com.shoppingcartsystem.services;

import com.shoppingcartsystem.payloads.ProductDto;
import com.shoppingcartsystem.payloads.ProductResponse;

public interface ProductService {

	ProductDto createProduct(ProductDto productDto, Integer categoryId, Integer merchantId);

	ProductDto updateProduct(ProductDto productDto, Integer productId);

	void deleteProduct(Integer productId);

	ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

	ProductDto getProductById(Integer productId);

	ProductResponse getProductByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy,
			String sortDir);

	ProductResponse searchProducts(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

	ProductResponse getByMerchantId(Integer merchantId, Integer pageNumber, Integer pageSize, String sortBy,
			String sortDir);
}
