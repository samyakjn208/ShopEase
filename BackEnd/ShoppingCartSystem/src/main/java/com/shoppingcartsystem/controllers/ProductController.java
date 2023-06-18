package com.shoppingcartsystem.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shoppingcartsystem.config.AppConstants;
import com.shoppingcartsystem.payloads.ApiResponse;
import com.shoppingcartsystem.payloads.ProductDto;
import com.shoppingcartsystem.payloads.ProductResponse;
import com.shoppingcartsystem.services.FileService;
import com.shoppingcartsystem.services.ProductService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private FileService fileService;

	@PreAuthorize("hasAnyAuthority('MERCHANT')")
	@PostMapping("/category/{categoryId}/product/{merchantId}")
	public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto,
			@PathVariable Integer categoryId, @PathVariable Integer merchantId) {
		ProductDto createdProduct = this.productService.createProduct(productDto, categoryId, merchantId);
		return new ResponseEntity<ProductDto>(createdProduct, HttpStatus.CREATED);
	}

	@GetMapping("/category/{categoryId}/products")
	public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Integer categoryId,
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {

		// List<ProductDto> products =
		// this.productService.getProductByCategory(categoryId);

		ProductResponse productResponse = this.productService.getProductByCategory(categoryId, pageNumber, pageSize,
				sortBy, sortDir);
		// return new ResponseEntity<List<ProductDto>>(products, HttpStatus.OK);
		return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.OK);
	}

	@GetMapping("/products")
	public ResponseEntity<ProductResponse> getAllProducts(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
		ProductResponse productResponse = this.productService.getAllProducts(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.OK);
	}

	@GetMapping("/products/{productId}")
	public ResponseEntity<ProductDto> getProductById(@PathVariable Integer productId) {
		ProductDto productDto = this.productService.getProductById(productId);
		return new ResponseEntity<ProductDto>(productDto, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@DeleteMapping("/products/{productId}")
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Integer productId) {
		this.productService.deleteProduct(productId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Product Deleted Successfully!", true), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('MERCHANT','ADMIN')")
	@PutMapping("/products/{productId}")
	public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto,
			@PathVariable Integer productId) {
		ProductDto updatedProduct = this.productService.updateProduct(productDto, productId);
		return new ResponseEntity<ProductDto>(updatedProduct, HttpStatus.OK);
	}

	@GetMapping("/products/search/{keyword}")
	public ResponseEntity<ProductResponse> searchProductByTitle(@PathVariable("keyword") String keyword,
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
		ProductResponse result = this.productService.searchProducts(keyword, pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<ProductResponse>(result, HttpStatus.OK);

	}

	@PreAuthorize("hasAnyAuthority('MERCHANT')")
	@PostMapping("/products/img_upload/{productId}")
	public ResponseEntity<ProductDto> uploadProductImage(@RequestParam("image") MultipartFile image,
			@PathVariable Integer productId) throws IOException {

		ProductDto productDto = this.productService.getProductById(productId);
		String fileName = this.fileService.uploadImage(image);

		List<String> productPhoto = productDto.getProductPhoto();
		productPhoto.add(fileName);
		productDto.setProductPhoto(productPhoto);

		ProductDto updatedProduct = this.productService.updateProduct(productDto, productId);
		return new ResponseEntity<ProductDto>(updatedProduct, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('MERCHANT')")
	@PostMapping("/products/multiImg_upload/{productId}")
	public ResponseEntity<ProductDto> uploadMultipleProductImages(@RequestParam("images") MultipartFile[] images,
			@PathVariable Integer productId) throws IOException {
		ProductDto productDto = this.productService.getProductById(productId);

		List<String> productPhoto = productDto.getProductPhoto();

		for (MultipartFile image : images) {
			String fileName = this.fileService.uploadImage(image);
			productPhoto.add(fileName);
		}

		productDto.setProductPhoto(productPhoto);

		ProductDto updatedProduct = this.productService.updateProduct(productDto, productId);
		return new ResponseEntity<ProductDto>(updatedProduct, HttpStatus.OK);
	}

	@GetMapping(value = "/products/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void viewImage(@PathVariable String imageName, HttpServletResponse response) throws IOException {

		InputStream resource = this.fileService.getResource(imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}

	@PreAuthorize("hasAnyAuthority('MERCHANT')")
	@GetMapping("/products/mId/{merchantId}")
	public ResponseEntity<ProductResponse> getProductsByMerchantId(@PathVariable Integer merchantId,
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {
		ProductResponse productDtos = this.productService.getByMerchantId(merchantId, pageNumber, pageSize, sortBy,
				sortDir);
		return new ResponseEntity<ProductResponse>(productDtos, HttpStatus.OK);
	}
}
