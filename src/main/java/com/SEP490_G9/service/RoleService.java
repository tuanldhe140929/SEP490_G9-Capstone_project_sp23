package com.SEP490_G9.service;

import com.SEP490_G9.entities.Role;

public interface RoleService {
	Role getRoleById(int roleId);

	Role getRoleByName(String roleName);
}
