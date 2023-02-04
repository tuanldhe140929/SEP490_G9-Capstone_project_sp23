package com.SEP490_G9.services;

import java.util.List;

import com.SEP490_G9.models.Entities.User;

public interface ManageInspectorService {
	public List<User> getAllInspectors();
	
	public boolean addInspector(User inspector);
	
	public boolean activateInspector(Long id);
	
	public boolean deactivateInspector(Long id);
	
}
