package com.shoppingcartsystem.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OAuthUserRequest {

	private String name;
	private String emailId;
	
}
