package com.SEP490_G9.services;

import java.util.List;

import com.SEP490_G9.models.Entities.Tag;

public interface ManageTagService {
	public List<Tag> getAllTags();
	
	public boolean addTag(Tag tag);
	
	public boolean updateTag(Tag tag, int id);
}
