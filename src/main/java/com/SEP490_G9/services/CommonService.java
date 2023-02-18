package com.SEP490_G9.services;

import com.SEP490_G9.models.Entities.User;

public interface CommonService {
	public User getUserInfoByUsername(String username);

	public User getCurrentLogedInUser();
}
