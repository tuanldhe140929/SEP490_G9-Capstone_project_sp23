package com.SEP490_G9.services.serviceImpls;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.SEP490_G9.exceptions.DuplicateFieldException;
import com.SEP490_G9.helpers.Constant;
import com.SEP490_G9.models.Entities.Account;
import com.SEP490_G9.models.Entities.Role;
import com.SEP490_G9.repositories.AccountRepository;
import com.SEP490_G9.repositories.RoleRepository;
import com.SEP490_G9.services.ManageStaffService;

@Service
public class ManageStaffServiceImpl implements ManageStaffService{

	@Autowired
	AccountRepository accountRepo;
	
	@Autowired
	RoleRepository roleRepo;

	@Override
	public List<Account> getAllStaffs() {
		List<Role> staffRoles = new ArrayList<>();
		staffRoles.add(roleRepo.findById(Constant.STAFF_ROLE_ID));
		List<Account> staffList = accountRepo.findByRolesIn(staffRoles);
		return staffList;
	}

	@Override
	public boolean addStaff(Account staff) {
		if(accountRepo.existsByEmail(staff.getEmail())) {
			throw new DuplicateFieldException("email", staff.getEmail());
		}
		String encodedPassword = new BCryptPasswordEncoder().encode(staff.getPassword().trim());
		staff.setPassword(encodedPassword);
		Date date = new Date();
		staff.setAccountCreatedDate(date);
		List<Role> staffRoles = new ArrayList<>();
		staffRoles.add(roleRepo.getReferenceById(Constant.STAFF_ROLE_ID));
		staff.setRoles(staffRoles);
		accountRepo.save(staff);
		return true;
	}

	@Override
	public boolean updateStaffStatus(Long id) {
		Account staff = accountRepo.findById(id).get();
		if(staff.isEnabled()) {
			staff.setEnabled(false);
		}else {
			staff.setEnabled(true);
		}
		accountRepo.save(staff);
		return true;
	}


}
