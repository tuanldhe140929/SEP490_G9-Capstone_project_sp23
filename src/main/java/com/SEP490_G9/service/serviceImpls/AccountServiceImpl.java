package com.SEP490_G9.service.serviceImpls;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entity.Account;
import com.SEP490_G9.entity.RefreshToken;
import com.SEP490_G9.entity.Role;
import com.SEP490_G9.exception.DuplicateFieldException;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.AccountRepository;
import com.SEP490_G9.repository.RoleRepository;
import com.SEP490_G9.service.AccountService;
import com.SEP490_G9.util.Constant;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	AccountRepository accountRepo;
	
	@Autowired
	RoleRepository roleRepo;
	
	@Override
	public Account getByRefreshToken(RefreshToken findByToken) {
		Account ret = accountRepo.findByRefreshToken(findByToken);
		if(ret==null) {
			throw new ResourceNotFoundException("account", "refresh token", findByToken.getToken());
		}
		return ret;
	}

	@Override
	public Account getById(Long id) {
		Account ret = accountRepo.findById(id).get();
		if(ret==null) {
			throw new ResourceNotFoundException("account", "id", id);
		}
		return ret;
	}

	@Override
	public Account getByEmail(String email) {
		Account ret = accountRepo.findByEmail(email);
		if(ret==null) {
			throw new ResourceNotFoundException("account", "email", email);
		}
		return ret;
	}

	@Override
	public Account update(Account account) {
		Account saved = accountRepo.save(account);
		return saved;
	}
	
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
