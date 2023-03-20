package com.SEP490_G9.service.serviceImpls;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entities.Account;
import com.SEP490_G9.entities.RefreshToken;
import com.SEP490_G9.entities.Role;
import com.SEP490_G9.exception.DuplicateFieldException;
import com.SEP490_G9.exception.EmailServiceException;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.AccountRepository;
import com.SEP490_G9.repository.RoleRepository;
import com.SEP490_G9.service.AccountService;
import com.SEP490_G9.common.Constant;
import com.SEP490_G9.common.PasswordGenerator;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	AccountRepository accountRepo;

	@Autowired
	RoleRepository roleRepo;

	@Autowired
	PasswordGenerator pg;

	@Override
	public Account getByRefreshToken(RefreshToken findByToken) {
		Account ret = accountRepo.findByRefreshToken(findByToken);
		if (ret == null) {
			throw new ResourceNotFoundException("account", "refresh token", findByToken.getToken());
		}
		return ret;
	}

	@Override
	public Account getById(Long id) {
		Account ret = accountRepo.findById(id).get();
		if (ret == null) {
			throw new ResourceNotFoundException("account", "id", id);
		}
		return ret;
	}

	@Override
	public Account getByEmail(String email) {
		Account ret = accountRepo.findByEmail(email);
		if (ret == null) {
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
	public Account addStaff(Account staff) {
		if (accountRepo.existsByEmail(staff.getEmail())) {
			throw new DuplicateFieldException("email", staff.getEmail());
		}
		if (!staff.getEmail()
				.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
			throw new EmailServiceException("Not a valid email");
		}
		String encodedPassword = new BCryptPasswordEncoder().encode(staff.getPassword().trim());
		staff.setPassword(encodedPassword);
		Date date = new Date();
		staff.setCreatedDate(new Date());
		List<Role> staffRoles = new ArrayList<>();
		staffRoles.add(roleRepo.getReferenceById(Constant.STAFF_ROLE_ID));
		staff.setRoles(staffRoles);
		accountRepo.save(staff);
		return staff;
	}

	@Override
	public Account updateStaffStatus(long id) {
		Account account = accountRepo.findById(id).get();
		if (account.isEnabled()) {
			account.setEnabled(false);
		} else {
			account.setEnabled(true);
		}
		accountRepo.save(account);
		return account;
	}

	@Override
	public String resetPassword(Account account) {
		String newPassword = pg.generatePassword(8).toString();
		String encodedPassword = new BCryptPasswordEncoder().encode(newPassword);
		account.setPassword(encodedPassword);
		accountRepo.save(account);
		return newPassword;
	}

}
