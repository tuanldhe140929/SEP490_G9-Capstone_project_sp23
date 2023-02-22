package com.SEP490_G9.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SEP490_G9.entity.Account;
import com.SEP490_G9.service.ManageStaffService;


@RequestMapping("private/manageStaff")
@RestController
public class ManageStaffController {

	@Autowired
	ManageStaffService manageStaffService;
	
	@GetMapping("staffs")
	public ResponseEntity<?> getAllStaffs(){
		List<Account> staffList = manageStaffService.getAllStaffs();
		return ResponseEntity.ok(staffList);
	}
	
	@PostMapping("addStaff")
	public ResponseEntity<?> addStaff(@RequestBody Account staff){
		boolean canAdd = manageStaffService.addStaff(staff);
		return ResponseEntity.ok(canAdd);
	}
	
	@PutMapping("updateStaffStatus/{id}")
	public ResponseEntity<?> updateStaffStatus(@PathVariable(name = "id") Long id){
		boolean canUpdate = manageStaffService.updateStaffStatus(id);
		return ResponseEntity.ok(canUpdate);
	}
}
