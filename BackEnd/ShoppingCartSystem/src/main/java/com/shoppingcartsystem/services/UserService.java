package com.shoppingcartsystem.services;

import java.util.List;

import com.shoppingcartsystem.payloads.OAuthUserRequest;
import com.shoppingcartsystem.payloads.UserDto;

public interface UserService {

	UserDto createUser(UserDto user);

	UserDto loginUser(UserDto user);

	UserDto updateUser(UserDto user, Integer userId);

	UserDto getUserById(Integer userId);

	List<UserDto> getAllUsers();

	void deleteUser(Integer userId);
	
	UserDto getUserByEmail (String emailId);

	UserDto singleSignOn(OAuthUserRequest oAuthUserRequest);
}
