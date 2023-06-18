package com.shoppingcartsystem.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingcartsystem.services.StatsService;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

	@Autowired
	private StatsService statsService;

	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@GetMapping("/admin")
	public ResponseEntity<Map<String, Object>> adminStats() {
		return new ResponseEntity<Map<String, Object>>(this.statsService.AdminStats(), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('MERCHANT')")
	@GetMapping("/merchant/{merchantId}")
	public ResponseEntity<Map<String, Object>> merchantStats(@PathVariable Integer merchantId) {
		return new ResponseEntity<Map<String, Object>>(this.statsService.MerchantStats(merchantId), HttpStatus.OK);
	}
}
