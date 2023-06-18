package com.shoppingcartsystem.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "jwt")
@Data

public class JwtProperties {

	private String secretKey = "rzxlszyykpbgqcflzxsqcysyhljt";

	// validity in milliseconds
	private long validityInMs = 86400000; // 1day

}
