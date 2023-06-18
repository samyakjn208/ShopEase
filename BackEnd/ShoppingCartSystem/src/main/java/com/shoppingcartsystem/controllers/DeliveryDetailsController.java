package com.shoppingcartsystem.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingcartsystem.payloads.ApiResponse;
import com.shoppingcartsystem.payloads.DeliveryDetailsDto;
import com.shoppingcartsystem.services.DeliveryDetailsService;

@RestController
@RequestMapping("/api/deliveryDetails")
public class DeliveryDetailsController {

	@Autowired
	private DeliveryDetailsService deliveryDetailsService;
	
	@PreAuthorize("hasAnyAuthority('USER')")
	@PostMapping("/")
	public ResponseEntity<DeliveryDetailsDto> addDeliveryDetails(@RequestBody DeliveryDetailsDto deliveryDetailsDto){
		DeliveryDetailsDto createdDeliveryDetailsDto = this.deliveryDetailsService.addDeliveryDetails(deliveryDetailsDto);
		return new ResponseEntity<DeliveryDetailsDto>(createdDeliveryDetailsDto, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<DeliveryDetailsDto>> viewAllDeliveryDetailsOfUser(@PathVariable Integer userId){
		List<DeliveryDetailsDto> viewAllDeliveryDetailsOfUser = this.deliveryDetailsService.viewAllDeliveryDetailsOfUser(userId);
		return new ResponseEntity<List<DeliveryDetailsDto>>(viewAllDeliveryDetailsOfUser, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@GetMapping("/")
	public ResponseEntity<List<DeliveryDetailsDto>> viewAllDeliveryDetails(){
		List<DeliveryDetailsDto> viewAllDeliveryDetails = this.deliveryDetailsService.viewAllDeliveryDetails();
		return new ResponseEntity<List<DeliveryDetailsDto>>(viewAllDeliveryDetails, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN','MERCHANT','USER')")
	@GetMapping("/{deliveryDetailsId}")
	public ResponseEntity<DeliveryDetailsDto> viewDeliveryDetailsById(@PathVariable Integer deliveryDetailsId){
		DeliveryDetailsDto viewAllDeliveryDetailsOfUser = this.deliveryDetailsService.viewDeliveryDetails(deliveryDetailsId);
		return new ResponseEntity<DeliveryDetailsDto>(viewAllDeliveryDetailsOfUser, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
	@PutMapping("/{deliveryDetailsId}")
	public ResponseEntity<DeliveryDetailsDto> updateDeliveryDetails(@RequestBody DeliveryDetailsDto deliveryDetailsDto, @PathVariable Integer deliveryDetailsId){
		DeliveryDetailsDto updatedDeliveryDetailsDto = this.deliveryDetailsService.updateDeliveryDetails(deliveryDetailsDto, deliveryDetailsId);
		return new ResponseEntity<DeliveryDetailsDto>(updatedDeliveryDetailsDto, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
	@DeleteMapping("/{deliveryDetailsId}")
	public ResponseEntity<ApiResponse> deleteDeliveryDetailsById(@PathVariable Integer deliveryDetailsId){
		this.deliveryDetailsService.removeDeliveryDetails(deliveryDetailsId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("DeliveryDetails deleted successfully!", true), HttpStatus.OK);
	}
	
}
