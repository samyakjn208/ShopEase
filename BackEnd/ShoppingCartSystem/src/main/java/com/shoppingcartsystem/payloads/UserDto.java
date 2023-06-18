package com.shoppingcartsystem.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {

	private int userId;

	@NotEmpty(message = "Name cannot be empty!")
	@Pattern(regexp = "^[a-zA-Z ]*", message = "Incorrect format for Name!")
	private String name;

	@NotEmpty(message = "Mobile cannot be empty!")
	@Size(min = 10, max = 10, message = "Enter 10 digits!")
	@Pattern(regexp = "^[0-9]*", message = "Enter digits only!")
	@Pattern(regexp = "^(9|8|7|6)[0-9]*$", message = "Incorrect Number!")

	private String mobileNumber;

	@NotEmpty(message = "Email ID cannot be empty!")
	@Email(message = "Kindly enter correct email id!")
	private String emailId;

	@NotEmpty(message = "Password cannot be empty!")
	@Size(min = 8, message = "Password length should be minimum 8")
	@Size(max = 16, message = "Password should not exceed length of 16")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-{}|:\"<>?/.,';\\[\\]])[A-Za-z\\d!@#$%^&*()_+=\\-{}|:\"<>?/.,';\\[\\]]+$", message = "Password should contain at least one upper-case letter, lower-case letter, a digit and a special character")
	private String password;
	private String role;

}
