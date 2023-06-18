package com.shoppingcartsystem.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingcartsystem.jwt.JwtTokenProvider;
import com.shoppingcartsystem.payloads.ApiResponse;
import com.shoppingcartsystem.payloads.OAuthUserRequest;
import com.shoppingcartsystem.payloads.UserDto;
import com.shoppingcartsystem.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@PostMapping("/signup")
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
		userDto.setRole("USER");
		UserDto createdUser = this.userService.createUser(userDto);

		return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
	}

	@PostMapping("/msignup")
	public ResponseEntity<UserDto> createMerchant(@Valid @RequestBody UserDto userDto) {
		userDto.setRole("MERCHANT");
		UserDto createdUser = this.userService.createUser(userDto);

		return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody UserDto userDto) {
		try {
			var authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmailId(), userDto.getPassword()));
			String token = jwtTokenProvider.createToken(authentication);

			UserDto user = this.userService.getUserByEmail(userDto.getEmailId());

			Map<String, Object> response = new HashMap<>();
			response.put("userId", user.getUserId());
			response.put("name", user.getName());
			response.put("token", token);
			response.put("role", user.getRole());

			return ResponseEntity.ok(response);

		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().body("Invalid username/password supplied");
		}
	}

	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, @PathVariable Integer userId) {
		UserDto updatedUser = this.userService.updateUser(userDto, userId);
		return ResponseEntity.ok(updatedUser);
	}

	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer userId) {
		this.userService.deleteUser(userId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("User deleted successfully!", true), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@GetMapping("/")
	public ResponseEntity<List<UserDto>> getAllUsers() {
		return ResponseEntity.ok(this.userService.getAllUsers());
	}

	@PreAuthorize("hasAnyAuthority('ADMIN','USER')")
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUser(@PathVariable Integer userId) {
		return ResponseEntity.ok(this.userService.getUserById(userId));
	}

	@PostMapping("/OAuth")
	public ResponseEntity<?> singleSignOn(@RequestBody OAuthUserRequest oAuthUserRequest) {

		UserDto user = this.userService.singleSignOn(oAuthUserRequest);

		String token = jwtTokenProvider.createTokenForSingleSignOn(user.getName());
		Map<String, Object> response = new HashMap<>();
		response.put("userId", user.getUserId());
		response.put("name", user.getName());
		response.put("role", user.getRole());
		response.put("token", token);
		return ResponseEntity.ok(response);
	}
}
