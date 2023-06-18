package com.shoppingcartsystem.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shoppingcartsystem.payloads.OAuthUserRequest;
import com.shoppingcartsystem.payloads.UserDto;
import com.shoppingcartsystem.repositories.UserRepository;
import com.shoppingcartsystem.services.UserService;
import com.shoppingcartsystem.entities.User;
import com.shoppingcartsystem.exceptions.EmailIdAlreadyExistsException;
import com.shoppingcartsystem.exceptions.ResourceNotFoundException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDto createUser(UserDto userDto){
		if(this.userRepository.findByEmailId(userDto.getEmailId()).isPresent()) {
			throw new EmailIdAlreadyExistsException();
		}
		User user = this.dtoToUser(userDto);
		user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
		User savedUser = this.userRepository.save(user);
		return this.userToDto(savedUser);
	}

	@Override
	public UserDto loginUser(UserDto userDto) {
		User user = this.dtoToUser(userDto);
		User loggedInUser = this.userRepository.findByEmailIdAndPassword(user.getEmailId(), user.getPassword());
		return this.userToDto(loggedInUser);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

		user.setName(userDto.getName());
		user.setMobileNumber(userDto.getMobileNumber());
		user.setEmailId(userDto.getEmailId());

		User updatedUser = this.userRepository.save(user);

		return this.userToDto(updatedUser);
	}

	@Override
	public UserDto getUserById(Integer userId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

		return this.userToDto(user);
	}

	@Override
	public List<UserDto> getAllUsers() {

		List<User> users = this.userRepository.findAll();
		List<UserDto> userDtos = users.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());

		return userDtos;
	}

	@Override
	public void deleteUser(Integer userId) {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

		this.userRepository.delete(user);
	}

	private User dtoToUser(UserDto userDto) {
		User user = this.modelMapper.map(userDto, User.class);
		return user;
	}

	private UserDto userToDto(User user) {
		UserDto userDto = this.modelMapper.map(user, UserDto.class);
		return userDto;
	}

	@Override
	public UserDto getUserByEmail(String emailId) {
		User user = this.userRepository.findByEmailId(emailId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "eMailId", 0));
		;
		return this.modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto singleSignOn(OAuthUserRequest oAuthUserRequest) {
		Optional<User> user = this.userRepository.findByEmailId(oAuthUserRequest.getEmailId());
		if(user.isEmpty()) {
			User newUser = new User();
			newUser.setName(oAuthUserRequest.getName());
			newUser.setEmailId(oAuthUserRequest.getEmailId());
			newUser.setRole("USER");
			User savedUser = this.userRepository.save(newUser);
			return this.modelMapper.map(savedUser, UserDto.class);
		}
		return this.modelMapper.map(user, UserDto.class);
	}

}
