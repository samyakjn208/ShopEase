package com.shoppingcartsystem.services;

import java.util.List;

import com.shoppingcartsystem.payloads.DeliveryDetailsDto;

public interface DeliveryDetailsService {

	DeliveryDetailsDto addDeliveryDetails (DeliveryDetailsDto deliveryDetailsDto);
	
	DeliveryDetailsDto updateDeliveryDetails (DeliveryDetailsDto deliveryDetailsDto, Integer deliveryDetailsId);
	
	DeliveryDetailsDto viewDeliveryDetails (Integer deliveryDetailsId);
	
	List<DeliveryDetailsDto> viewAllDeliveryDetailsOfUser (Integer userId);
	
	void removeDeliveryDetails (Integer deliveryDetailsId);

	List<DeliveryDetailsDto> viewAllDeliveryDetails();
}
