package com.SEP490_G9.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.dto.PayoutDTO;
import com.SEP490_G9.entities.Payout;
import com.SEP490_G9.service.PayoutService;

@RequestMapping(value = "payout")
@RestController

public class PayoutController {
	
@Autowired
PayoutService payoutService;

@GetMapping("getPayoutHistory")
public ResponseEntity<?> getPayoutHistory(){
	List<Payout> allStatusPayout = payoutService.getPayoutHistory();
	List<PayoutDTO> allDtoPayout = new ArrayList<>();
	for(Payout po: allStatusPayout) {
		allDtoPayout.add(new PayoutDTO(po));
	}
	return ResponseEntity.ok(allDtoPayout);
}

}
