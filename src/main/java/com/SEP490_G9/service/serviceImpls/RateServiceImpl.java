package com.SEP490_G9.service.serviceImpls;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entities.Rate;
import com.SEP490_G9.entities.embeddable.RateItemKey;
import com.SEP490_G9.repository.RateRepository;
import com.SEP490_G9.service.RateService;


@Service
public class RateServiceImpl implements RateService  {
	@Autowired
	private RateRepository rateRepository;

	@Override
	public List<Rate> getAllRates() {
		return rateRepository.findAll();
	}

	@Override
	public void saveRate(Rate rate) {
		rateRepository.save(rate);
	}

	@Override
	public void deleteRate(Long userID, Long productID) {
		RateItemKey key = new RateItemKey(userID, productID);
		rateRepository.deleteById(key);
	}

	@Override
	public Optional<Rate> findRateById(Long userID, Long productID) {
		RateItemKey key = new RateItemKey(userID, productID);
		return rateRepository.findById(key);
	}

	@Override
	public void saveEditedRate(Rate rateEdit) {
		rateRepository.save(rateEdit);
	}
}
