package com.shoppingcartsystem.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailIdAlreadyExistsException extends RuntimeException {

	public EmailIdAlreadyExistsException() {
		super(String.format("Email already exists!"));
	}
}
