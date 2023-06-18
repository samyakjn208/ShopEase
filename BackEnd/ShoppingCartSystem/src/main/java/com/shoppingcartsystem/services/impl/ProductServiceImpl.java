package com.shoppingcartsystem.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shoppingcartsystem.entities.Category;
import com.shoppingcartsystem.entities.Product;
import com.shoppingcartsystem.entities.User;
import com.shoppingcartsystem.exceptions.ResourceNotFoundException;
import com.shoppingcartsystem.payloads.ProductDto;
import com.shoppingcartsystem.payloads.ProductResponse;
import com.shoppingcartsystem.repositories.CategoryRepository;
import com.shoppingcartsystem.repositories.ProductRepository;
import com.shoppingcartsystem.repositories.UserRepository;
import com.shoppingcartsystem.services.FileService;
import com.shoppingcartsystem.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private FileService fileService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public ProductDto createProduct(ProductDto productDto, Integer categoryId, Integer merchantId) {

		User user = this.userRepository.findById(merchantId)
				.orElseThrow(() -> new ResourceNotFoundException("Merchant", "Id", merchantId));

		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Id", categoryId));

		if (user.getRole().equals("MERCHANT")) {
			Product product = this.modelMapper.map(productDto, Product.class);
			product.setMerchantId(merchantId);
			product.setCategory(category);
			List<String> productPhoto = new ArrayList<>();
			product.setProductPhoto(productPhoto);
			if (product.getStock() == null) {
				product.setStock(0);
			}
			Product savedProduct = this.productRepository.save(product);
			return this.modelMapper.map(savedProduct, ProductDto.class);
		} else {
			throw new ResourceNotFoundException("Merchant", "Id", merchantId);
		}
	}

	@Override
	public ProductDto updateProduct(ProductDto productDto, Integer productId) {
		Product product = this.productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "Product Id", productId));

		product.setProductName(productDto.getProductName());
		product.setProductDescription(productDto.getProductDescription());
		product.setProductPhoto(productDto.getProductPhoto());
		product.setProductMRP(productDto.getProductMRP());
		product.setProductPrice(productDto.getProductPrice());
		product.setStock(productDto.getStock());

		Product savedProduct = this.productRepository.save(product);

		return this.modelMapper.map(savedProduct, ProductDto.class);
	}

	@Override
	public void deleteProduct(Integer productId) {
		Product product = this.productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "Product Id", productId));

		List<String> productPhotos = product.getProductPhoto();
		for (String productPhoto : productPhotos) {
			this.fileService.deleteImage(productPhoto);
		}

		this.productRepository.delete(product);
	}

	@Override
	public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> productPage = this.productRepository.findAll(p);
		List<Product> allProducts = productPage.getContent();
		List<ProductDto> productDtos = allProducts.stream()
				.map((product) -> this.modelMapper.map(product, ProductDto.class))
				.filter(productDto -> productDto.getStock() != 0).collect(Collectors.toList());
		ProductResponse productResponse = new ProductResponse();
		productResponse.setContent(productDtos);
		productResponse.setPageNumber(productPage.getNumber());
		productResponse.setPageSize(productPage.getSize());
		productResponse.setTotalElements(productPage.getTotalElements());
		productResponse.setTotalPages(productPage.getTotalPages());
		productResponse.setLastPage(productPage.isLast());

		return productResponse;
	}

	@Override
	public ProductDto getProductById(Integer productId) {
		Product product = this.productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "Product Id", productId));
		return this.modelMapper.map(product, ProductDto.class);
	}

	@Override
	public ProductResponse getProductByCategory(Integer categoryId, Integer pageNumber, Integer pageSize, String sortBy,
			String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

		Pageable p = PageRequest.of(pageNumber, pageSize, sort);

		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Id", categoryId));

		Page<Product> productPage = this.productRepository.findByCategory(category, p);
		List<Product> allProducts = productPage.getContent();
		List<ProductDto> productDtos = allProducts.stream()
				.map((product) -> this.modelMapper.map(product, ProductDto.class))
				.filter(productDto -> productDto.getStock() != 0).collect(Collectors.toList());

		ProductResponse productResponse = new ProductResponse();
		productResponse.setContent(productDtos);
		productResponse.setPageNumber(productPage.getNumber());
		productResponse.setPageSize(productPage.getSize());
		productResponse.setTotalElements(productPage.getTotalElements());
		productResponse.setTotalPages(productPage.getTotalPages());
		productResponse.setLastPage(productPage.isLast());

		return productResponse;
	}

	@Override
	public ProductResponse searchProducts(String query, Integer pageNumber, Integer pageSize, String sortBy,
			String sortDir) {
		String cleanedQuery = query.replaceAll("[^a-zA-Z0-9 ]", "");
		String[] keywords = cleanedQuery.toLowerCase().split("\\s+");
		String formattedQuery = "%" + String.join("%", keywords) + "%";

		Sort sort = (sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

		Pageable p = PageRequest.of(pageNumber, pageSize, sort);

		Page<Product> productPage = this.productRepository.search(formattedQuery, null);
		List<Product> allProducts = productPage.getContent();
		List<ProductDto> productDtos = allProducts.stream()
				.map((product) -> this.modelMapper.map(product, ProductDto.class))
				.filter(productDto -> productDto.getStock() != 0).collect(Collectors.toList());

		ProductResponse productResponse = new ProductResponse();
		productResponse.setContent(productDtos);
		productResponse.setPageNumber(productPage.getNumber());
		productResponse.setPageSize(productPage.getSize());
		productResponse.setTotalElements(productPage.getTotalElements());
		productResponse.setTotalPages(productPage.getTotalPages());
		productResponse.setLastPage(productPage.isLast());
		return productResponse;
	}

	@Override
	public ProductResponse getByMerchantId(Integer merchantId, Integer pageNumber, Integer pageSize, String sortBy,
			String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

		Pageable p = PageRequest.of(pageNumber, pageSize, sort);

		Page<Product> productPage = this.productRepository.findByMerchantId(merchantId, p);
		List<Product> allProducts = productPage.getContent();
		List<ProductDto> productDtos = allProducts.stream()
				.map((product) -> this.modelMapper.map(product, ProductDto.class)).collect(Collectors.toList());

		ProductResponse productResponse = new ProductResponse();
		productResponse.setContent(productDtos);
		productResponse.setPageNumber(productPage.getNumber());
		productResponse.setPageSize(productPage.getSize());
		productResponse.setTotalElements(productPage.getTotalElements());
		productResponse.setTotalPages(productPage.getTotalPages());
		productResponse.setLastPage(productPage.isLast());
		return productResponse;
	}

}
