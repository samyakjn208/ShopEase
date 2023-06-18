package com.shoppingcartsystem.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "delivery_details")
@NoArgsConstructor
@Getter
@Setter
public class DeliveryDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int deliveryDetailsId;
	private String name;
	private String mobile;
	private String addressLine1;
	private String addressLine2;
	private int pincode;
	private String city;
	private String state;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
}
