package com.shoppingcartsystem.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shoppingcartsystem.entities.DeliveryDetails;
import com.shoppingcartsystem.entities.User;
import com.shoppingcartsystem.exceptions.ResourceNotFoundException;
import com.shoppingcartsystem.payloads.DeliveryDetailsDto;
import com.shoppingcartsystem.repositories.DeliveryDetailsRepository;
import com.shoppingcartsystem.repositories.UserRepository;
import com.shoppingcartsystem.services.DeliveryDetailsService;

@Service
public class DeliveryDetailsServiceImpl implements DeliveryDetailsService {

	@Autowired
	private DeliveryDetailsRepository deliveryDetailsRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public DeliveryDetailsDto addDeliveryDetails(DeliveryDetailsDto deliveryDetailsDto) {
		User user = this.userRepository.findById(deliveryDetailsDto.getTempUserId()).orElseThrow(() -> new ResourceNotFoundException("User", "Id", deliveryDetailsDto.getTempUserId()));
		DeliveryDetails deliveryDetails = this.modelMapper.map(deliveryDetailsDto, DeliveryDetails.class);
		deliveryDetails.setUser(user);
		DeliveryDetails savedDeliveryDetails = this.deliveryDetailsRepository.save(deliveryDetails);
		return this.modelMapper.map(savedDeliveryDetails, DeliveryDetailsDto.class);
	}

	@Override
	public DeliveryDetailsDto updateDeliveryDetails(DeliveryDetailsDto deliveryDetailsDto, Integer deliveryDetailsId) {
		DeliveryDetails deliveryDetails = this.deliveryDetailsRepository.findById(deliveryDetailsId)
				.orElseThrow(() -> new ResourceNotFoundException("DeliveryDetails", "Id", deliveryDetailsId));
		
		deliveryDetails.setName(deliveryDetailsDto.getName());	
		deliveryDetails.setMobile(deliveryDetailsDto.getMobile());
		deliveryDetails.setAddressLine1(deliveryDetailsDto.getAddressLine1());
		deliveryDetails.setAddressLine2(deliveryDetailsDto.getAddressLine2());
		deliveryDetails.setPincode(deliveryDetailsDto.getPincode());
		deliveryDetails.setCity(deliveryDetailsDto.getCity());
		deliveryDetails.setState(deliveryDetailsDto.getState());
		DeliveryDetails savedDeliveryDetails = this.deliveryDetailsRepository.save(deliveryDetails);
		return this.modelMapper.map(savedDeliveryDetails, DeliveryDetailsDto.class);
	}

	@Override
	public DeliveryDetailsDto viewDeliveryDetails(Integer deliveryDetailsId) {
		DeliveryDetails deliveryDetails = this.deliveryDetailsRepository.findById(deliveryDetailsId)
				.orElseThrow(() -> new ResourceNotFoundException("DeliveryDetails", "Id", deliveryDetailsId));
		return this.modelMapper.map(deliveryDetails, DeliveryDetailsDto.class);
	}

	@Override
	public List<DeliveryDetailsDto> viewAllDeliveryDetailsOfUser(Integer userId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
		List<DeliveryDetails> deliveryDetails = this.deliveryDetailsRepository.findByUser(user);
		List<DeliveryDetailsDto> deliveryDetailsDtos = deliveryDetails.stream()
				.map(deliveryDetail -> this.modelMapper.map(deliveryDetail, DeliveryDetailsDto.class))
				.collect(Collectors.toList());
		return deliveryDetailsDtos;
	}

	@Override
	public void removeDeliveryDetails(Integer deliveryDetailsId) {
		DeliveryDetails deliveryDetails = this.deliveryDetailsRepository.findById(deliveryDetailsId)
				.orElseThrow(() -> new ResourceNotFoundException("DeliveryDetails", "Id", deliveryDetailsId));
		this.deliveryDetailsRepository.delete(deliveryDetails);
	}

	@Override
	public List<DeliveryDetailsDto> viewAllDeliveryDetails() {
		List<DeliveryDetails> deliveryDetails = this.deliveryDetailsRepository.findAll();
		List<DeliveryDetailsDto> deliveryDetailsDtos = deliveryDetails.stream()
				.map(deliveryDetail -> this.modelMapper.map(deliveryDetail, DeliveryDetailsDto.class))
				.collect(Collectors.toList());
		return deliveryDetailsDtos;
	}

}
