package com.shoppingcartsystem.services;

import java.util.Map;

public interface StatsService {

	Map<String, Object> AdminStats();
	
	Map<String, Object> MerchantStats(Integer merchantId);
}
