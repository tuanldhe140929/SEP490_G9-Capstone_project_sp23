package com.SEP490_G9.service;


import java.util.List;
import java.util.Optional;

import com.SEP490_G9.entities.Rate;

public interface RateService {
	List<Rate> getAllRates();

	void saveRate(Rate rate);

	void deleteRate(Long userID, Long productID);

	Optional<Rate> findRateById(Long userID, Long productID);

	void saveEditedRate(Rate rateEdit);
}
