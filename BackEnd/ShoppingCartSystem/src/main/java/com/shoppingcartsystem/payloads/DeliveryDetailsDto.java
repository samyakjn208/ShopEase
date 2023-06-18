package com.shoppingcartsystem.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DeliveryDetailsDto {

	private int deliveryDetailsId;
	private String name;
	private String mobile;
	private String addressLine1;
	private String addressLine2;
	private int pincode;
	private String city;
	private String state;
	private int tempUserId;
	private UserDto user;
	
}
