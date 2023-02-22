package com.SEP490_G9.service;

import java.util.List;

import com.SEP490_G9.entity.User;

public interface ManageInspectorService {
	public List<User> getAllInspectors();
	
	public boolean addInspector(User inspector);
	
	public boolean activateInspector(Long id);
	
	public boolean deactivateInspector(Long id);
	
	public boolean deleteInspector(Long id);
	
	public boolean updateInspector(User inspector);
	
}
