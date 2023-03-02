package com.SEP490_G9.service;

import com.SEP490_G9.entity.Role;

public interface RoleService {
	Role getRoleById(int roleId);

	Role getRoleByName(String roleName);
}
