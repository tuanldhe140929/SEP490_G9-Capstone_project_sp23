package com.SEP490_G9.service.serviceImpls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.SEP490_G9.entity.Role;
import com.SEP490_G9.exception.ResourceNotFoundException;
import com.SEP490_G9.repository.RoleRepository;
import com.SEP490_G9.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	RoleRepository roleRepo;

	@Override
	public Role getRoleById(int roleId) {
		Role role = roleRepo.findById(roleId);
		if(role==null) {
			throw new ResourceNotFoundException("Role", "role id", roleId);
		}
		return role;
	}

	@Override
	public Role getRoleByName(String roleName) {
		Role role = roleRepo.findByName(roleName);
		if(role==null) {
			throw new ResourceNotFoundException("Role", "role name", roleName);
		}
		return role;
	}

}
